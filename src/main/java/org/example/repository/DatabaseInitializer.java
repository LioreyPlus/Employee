package org.example.repository;

import org.springframework.stereotype.Component;
import java.sql.*;

@Component
public class DatabaseInitializer {

    private static final String DB_URL = "jdbc:sqlite:src/main/resources/Employee.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC driver not found", e);
        }
    }

    public void initializeDatabase() throws SQLException {
        try (Connection con = DriverManager.getConnection(DB_URL);
             Statement stmt = con.createStatement()) {

            String createEmployeesTable = """
                CREATE TABLE IF NOT EXISTS employees (
                      id INTEGER PRIMARY KEY,
                      name TEXT NOT NULL,
                      salary INTEGER NOT NULL,
                      schedule_type TEXT NOT NULL,
                      title TEXT NOT NULL,
                      role TEXT NOT NULL,
                      chief_id INTEGER,
                      branch_id INTEGER NOT NULL,
                      FOREIGN KEY (chief_id) REFERENCES employees (id),
                      FOREIGN KEY (branch_id) REFERENCES branches (id)
                )
            """;

            String createBranchesTable = """
                CREATE TABLE IF NOT EXISTS branches (
                       id INTEGER PRIMARY KEY,
                       name TEXT NOT NULL,
                       address TEXT NOT NULL,
                       manager_id INTEGER,
                       FOREIGN KEY (manager_id) REFERENCES employees (id)
                )
            """;

            String createProjectsTable = """
                    CREATE TABLE IF NOT EXISTS projects (
                           id INTEGER PRIMARY KEY,
                           name TEXT NOT NULL
                           )
                    """;

            String createEmployeesProjectTable = """
                    CREATE TABLE IF NOT EXISTS employee_projects (
                        employee_id INT,
                        project_id INT,
                        PRIMARY KEY (employee_id, project_id),
                        FOREIGN KEY (employee_id) REFERENCES employees(id),
                        FOREIGN KEY (project_id) REFERENCES projects(id)
                        )
                    """;

            stmt.execute(createBranchesTable);
            stmt.execute(createEmployeesTable);
            stmt.execute(createProjectsTable);
            stmt.execute(createEmployeesProjectTable);
        }
    }

    public boolean hasEmployees() throws SQLException {
        String query = "SELECT COUNT(*) as count FROM employees";
        try (Connection con = DriverManager.getConnection(DB_URL);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.getInt("count") > 0;
        }
    }
}