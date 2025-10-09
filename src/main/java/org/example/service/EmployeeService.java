package org.example.service;

import org.example.Employee;
import org.example.repository.EmployeeCrudRepository;
import org.example.repository.EmployeeQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeCrudRepository employeeCrudRepository;

    @Autowired
    private EmployeeQueryRepository employeeQueryRepository;

    public List<Employee> getAllEmployees() {
        try {
            return employeeCrudRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении сотрудников", e);
        }
    }

    public Optional<Employee> getEmployeeById(int id) {
        try {
            return employeeCrudRepository.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске сотрудника по ID: " + id, e);
        }
    }

    public List<Employee> getEmployeesByRole(String role) {
        try {
            return employeeCrudRepository.findByRole(role);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске сотрудников по роли: " + role, e);
        }
    }

    public List<Employee> getEmployeesByBranch(int branchId) {
        try {
            return employeeQueryRepository.findByBranchId(branchId);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске сотрудников по филиалу: " + branchId, e);
        }
    }

    public List<Employee> searchEmployeesByName(String name) {
        try {
            return employeeQueryRepository.findByNameContaining(name);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске сотрудников по имени: " + name, e);
        }
    }

    public List<Employee> getEmployeesBySalaryRange(int minSalary, int maxSalary) {
        try {
            return employeeQueryRepository.findBySalaryRange(minSalary, maxSalary);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске сотрудников по зарплате", e);
        }
    }
}