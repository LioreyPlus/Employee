package org.example.repository;

import org.example.Employee;
import org.example.Role;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.*;

@Repository
public class EmployeeCrudRepository {

    private static final String DB_URL = "jdbc:sqlite:src/main/resources/Employee.db";

    public List<Employee> findAll() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        Map<Integer, Employee> employeeMap = new HashMap<>();
        Map<Integer, Integer> chiefRelations = new HashMap<>();

        String query = "SELECT id, name, salary, schedule_type, title, role, chief_id, branch_id FROM employees";

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
                int branchId = rs.getInt("branch_id");

                Employee employee = Employee.builder()
                        .id(id)
                        .name(name)
                        .salary(salary)
                        .scheduleType(scheduleType)
                        .title(title)
                        .role(role)
                        .branchId(branchId)
                        .build();

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
            if (employee != null && chief != null) {
                employee.setChief(chief);
            }
        }

        return employees;
    }

    public Optional<Employee> findById(int id) throws SQLException {
        return findAll().stream()
                .filter(emp -> emp.getId() == id)
                .findFirst();
    }

    public List<Employee> findByRole(String role) throws SQLException {
        return findAll().stream()
                .filter(emp -> emp.getRole().name().equalsIgnoreCase(role))
                .toList();
    }

    public void saveAll(List<Employee> employees) throws SQLException {
        String query = """
            INSERT OR REPLACE INTO employees (id, name, salary, schedule_type, title, role, chief_id, branch_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
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

                pstmt.setInt(8, employee.getBranchId());
                pstmt.addBatch();
            }

            pstmt.executeBatch();
        }
    }
}