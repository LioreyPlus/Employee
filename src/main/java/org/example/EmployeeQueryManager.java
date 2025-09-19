package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeQueryManager {
    private static final String DB_URL = "jdbc:sqlite:src/main/resources/Employee.db";


    public static List<Employee> getEmployeesBySalaryRange(int minSalary, int maxSalary) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        Map<Integer, Employee> employeeMap = new HashMap<>();

        String query = "SELECT id, name, salary, schedule_type, title, role, chief_id " +
                "FROM employees WHERE salary BETWEEN ? AND ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, minSalary);
            pstmt.setInt(2, maxSalary);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Employee employee = resultSetToEmployee(rs);
                    employees.add(employee);
                }
            }
        }
        for (Employee emp : employees) {
            employeeMap.put(emp.getId(), emp);
        }
        return employees;
    }

    public static List<Employee> getManagersBySalaryRange(int minSalary, int maxSalary) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT id, name, salary, schedule_type, title, role, chief_id " +
                "FROM employees WHERE role = 'MANAGER' AND salary BETWEEN ? AND ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, minSalary);
            pstmt.setInt(2, maxSalary);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Employee employee = resultSetToEmployee(rs);
                    employees.add(employee);
                }
            }
        }
        return employees;
    }

    public static Map<String, Integer> getBranchesWithEmployeeCount() throws SQLException {
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

    private static Employee resultSetToEmployee(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        int salary = rs.getInt("salary");
        String scheduleType = rs.getString("schedule_type");
        String title = rs.getString("title");
        Role role = Role.valueOf(rs.getString("role").toUpperCase());

        Employee employee = new Employee(id, name, salary, scheduleType, title, role, null);
        int chiefId = rs.getInt("chief_id");
        return employee;
    }

    public static List<String> getEmployeesByProject(String projectName) throws SQLException {
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

    public static List<String> getProjectsByEmployee(String employeeName) throws SQLException {
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

    public static List<String> getProjectsForTopDevelopersManagers() throws SQLException {
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
