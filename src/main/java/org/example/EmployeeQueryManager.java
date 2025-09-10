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
}
