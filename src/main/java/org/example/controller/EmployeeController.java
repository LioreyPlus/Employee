package org.example.controller;

import org.example.Employee;
import org.example.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable int id) {
        return employeeService.getEmployeeById(id).orElse(null);
    }

    @GetMapping("/role/{role}")
    public List<Employee> getEmployeesByRole(@PathVariable String role) {
        return employeeService.getEmployeesByRole(role);
    }

    @GetMapping("/branch/{branchId}")
    public List<Employee> getEmployeesByBranch(@PathVariable int branchId) {
        return employeeService.getEmployeesByBranch(branchId);
    }

    @GetMapping("/search")
    public List<Employee> searchEmployees(@RequestParam String name) {
        return employeeService.searchEmployeesByName(name);
    }

    @GetMapping("/salary-range")
    public List<Employee> getEmployeesBySalaryRange(
            @RequestParam int minSalary,
            @RequestParam int maxSalary) {
        return employeeService.getEmployeesBySalaryRange(minSalary, maxSalary);
    }

    @GetMapping("/health")
    public String health() {
        return "Employee API is running with Spring Boot!";
    }
}