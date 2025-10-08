package org.example.repository;

import org.example.Employee;
import org.example.EmployeeDataBaseManager;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeRepository {

    private List<Employee> employees = new ArrayList<>();

    public EmployeeRepository() {
        loadEmployeesFromDatabase();
    }

    private void loadEmployeesFromDatabase() {
        try {
            this.employees = EmployeeDataBaseManager.readAllEmployees();
            System.out.println(employees.size() + " employees have been uploaded from the database");
        } catch (SQLException e) {
            System.err.println("Error loading employees from the database: " + e.getMessage());
        }
    }

    public List<Employee> findAll() {
        return new ArrayList<>(employees);
    }

    public Optional<Employee> findById(int id) {
        return employees.stream()
                .filter(emp -> emp.getId() == id)
                .findFirst();
    }

    public List<Employee> findByRole(String role) {
        return employees.stream()
                .filter(emp -> emp.getRole().name().equalsIgnoreCase(role))
                .toList();
    }

    public List<Employee> findByBranchId(int branchId) {
        return employees.stream()
                .filter(emp -> emp.getBranchId() == branchId)
                .toList();
    }

    public List<Employee> findByNameContaining(String name) {
        return employees.stream()
                .filter(emp -> emp.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    public void refresh() {
        loadEmployeesFromDatabase();
    }
}