package org.example.repository;

import org.example.Project;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Map;

@Repository
public class ProjectRepository {

    private static final String DB_URL = "jdbc:sqlite:src/main/resources/Employee.db";

    public void saveAll(List<Project> projects) throws SQLException {
        String query = """
            INSERT OR REPLACE INTO projects (id, name)
            VALUES (?, ?)
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            for (Project project : projects) {
                pstmt.setInt(1, project.getId());
                pstmt.setString(2, project.getName());
                pstmt.addBatch();
            }

            pstmt.executeBatch();
        }
    }

    public void saveEmployeeProjects(Map<Integer, List<Integer>> assignment) throws SQLException {
        String query = """
            INSERT OR REPLACE INTO employee_projects (employee_id, project_id)
            VALUES (?, ?)
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            for (Map.Entry<Integer, List<Integer>> entry : assignment.entrySet()) {
                int employeeId = entry.getKey();
                List<Integer> projectIds = entry.getValue();

                for (int projectId : projectIds) {
                    pstmt.setInt(1, employeeId);
                    pstmt.setInt(2, projectId);
                    pstmt.addBatch();
                }
            }

            pstmt.executeBatch();
        }
    }
}