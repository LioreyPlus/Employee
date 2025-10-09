package org.example.repository;

import org.example.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class EmployeeQueryRepository {

    private static final String DB_URL = "jdbc:sqlite:src/main/resources/Employee.db";

    @Autowired
    private EmployeeCrudRepository employeeCrudRepository;

    public List<Employee> findByBranchId(int branchId) throws SQLException {
        return employeeCrudRepository.findAll().stream()
                .filter(emp -> emp.getBranchId() == branchId)
                .toList();
    }

    public List<Employee> findByNameContaining(String name) throws SQLException {
        return employeeCrudRepository.findAll().stream()
                .filter(emp -> emp.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    public List<Employee> findBySalaryRange(int minSalary, int maxSalary) throws SQLException {
        return employeeCrudRepository.findAll().stream()
                .filter(emp -> emp.getSalary() >= minSalary && emp.getSalary() <= maxSalary)
                .toList();
    }
}