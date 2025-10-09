package org.example.repository;

import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class StatisticsRepository {

    private static final String DB_URL = "jdbc:sqlite:src/main/resources/Employee.db";

    public Map<String, Integer> getBranchesWithEmployeeCount() throws SQLException {
        Map<String, Integer> branchStats = new HashMap<>();
        String query = """
            SELECT 
                b.name AS branch_name,
                COUNT(e.branch_id) AS employee_count
            FROM branches b
            LEFT JOIN employees e ON b.id = e.branch_id
            GROUP BY b.id, b.name
            ORDER BY employee_count DESC
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String branchName = rs.getString("branch_name");
                int employeeCount = rs.getInt("employee_count");
                branchStats.put(branchName, employeeCount);
            }
        }
        return branchStats;
    }

    public List<String> findEmployeesByProject(String projectName) throws SQLException {
        List<String> employeesByProject = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT e.name " +
                             "FROM employees e " +
                             "JOIN employee_projects ep ON e.ID = ep.employee_ID " +
                             "JOIN projects p ON ep.project_ID = p.ID " +
                             "WHERE p.name = ?"
             )) {

            stmt.setString(1, projectName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                employeesByProject.add(rs.getString("name"));
            }
        }

        return employeesByProject;
    }

    public List<String> findProjectsByEmployee(String employeeName) throws SQLException {
        List<String> projectsByEmployee = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT p.name " +
                             "FROM projects p " +
                             "JOIN employee_projects ep ON p.ID = ep.project_ID " +
                             "JOIN employees e ON ep.employee_ID = e.ID " +
                             "WHERE e.name = ?"
             )) {

            stmt.setString(1, employeeName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                projectsByEmployee.add(rs.getString("name"));
            }
        }

        return projectsByEmployee;
    }

    public List<String> findProjectsForTopDevelopersManagers() throws SQLException {
        List<String> projects = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(
                     "WITH TopDevelopers AS (" +
                             "    SELECT chief_id " +
                             "    FROM employees " +
                             "    WHERE role = 'DEVELOPER' " +
                             "    ORDER BY salary DESC " +
                             "    LIMIT 10" +
                             ")" +
                             "SELECT DISTINCT p.name as project_name " +
                             "FROM TopDevelopers td " +
                             "JOIN employees m ON td.chief_id = m.ID " +
                             "JOIN employee_projects ep ON m.ID = ep.employee_ID " +
                             "JOIN projects p ON ep.project_ID = p.ID " +
                             "ORDER BY p.name"
             )) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                projects.add(rs.getString("project_name"));
            }
        }

        return projects;
    }
}