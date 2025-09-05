package org.example;

import java.sql.*;
import java.util.*;

public class EmployeeDataBaseManager {
    private static String DB_URL = "jdbc:sqlite:src/main/resources/Employee.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC driver not found", e);
        }
    }

    public static void initiazlizeDatabase() throws SQLException {
        try (Connection con = DriverManager.getConnection(DB_URL);
             Statement stmt = con.createStatement()) {

            String createTable = """
                                    CREATE TABLE IF NOT EXISTS employees (
                                        id INTEGER PRIMARY KEY,
                                        name TEXT NOT NULL,
                                        salary INTEGER NOT NULL,
                                        schedule_type TEXT NOT NULL,
                                        title TEXT NOT NULL,
                                        role TEXT NOT NULL,
                                        chief_id INTEGER,
                                        FOREIGN KEY (chief_id) REFERENCES employees (id)
                                    )
                    """;

            stmt.execute(createTable);
        }
    }

    public static boolean hasEmployees() throws SQLException {
        String query = "SELECT COUNT(*) as count FROM employees";

        try (Connection con = DriverManager.getConnection(DB_URL);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            return rs.getInt("count") > 0;
        }
    }

    public static List<Employee> readAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        Map<Integer, Employee> employeeMap = new HashMap<>();
        Map<Integer, Integer> chiefRelations = new HashMap<>();

        String query = "SELECT id, name, salary, schedule_type, title, role, chief_id FROM employees";

        try (Connection con = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = con.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int salary = rs.getInt("salary");
                String scheduleType = rs.getString("schedule_type");
                String title = rs.getString("title");
                Role role = Role.valueOf(rs.getString("role").toUpperCase());

                Employee employee = new Employee(id, name, salary, scheduleType, title, role, null);
                employees.add(employee);
                employeeMap.put(id, employee);

                int chiefId = rs.getInt("chief_id");
                if (!rs.wasNull()) {
                    chiefRelations.put(id, chiefId);
                }
            }
        }
        for (Map.Entry<Integer, Integer> entry : chiefRelations.entrySet()) {
            Employee employee = employeeMap.get(entry.getKey());
            Employee chief = employeeMap.get(entry.getValue());
            if (chief != null) {
                employee.setChief(chief);
            }
        }
        return employees;
    }

    public static void saveEmployees(List<Employee> employees) throws SQLException {
        String query = """
            INSERT OR REPLACE INTO employees (id, name, salary, schedule_type, title, role, chief_id)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            for (Employee employee : employees) {
                pstmt.setInt(1, employee.getId());
                pstmt.setString(2, employee.getName());
                pstmt.setInt(3, employee.getSalary());
                pstmt.setString(4, employee.getScheduleType());
                pstmt.setString(5, employee.getTitle());
                pstmt.setString(6, employee.getRole().toString());

                if (employee.getChief() != null) {
                    pstmt.setInt(7, employee.getChief().getId());
                } else {
                    pstmt.setNull(7, Types.INTEGER);
                }

                pstmt.addBatch();
            }

            pstmt.executeBatch();
        }
    }
}
