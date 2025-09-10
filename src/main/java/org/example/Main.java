package org.example;

import java.io.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;



public class Main {
    public static void main(String[] args) throws IOException {
        String scheduleListPath = "src/main/resources/schedule.csv";
        List<Employee> employees = new ArrayList<>();
        List<Branch> branches = new ArrayList<>();
        Map<Integer, List<LocalDate>> employeesSchedule = new HashMap<>();


        try {
            EmployeeDataBaseManager.initializeDatabase();

            if (!EmployeeDataBaseManager.hasEmployees()) {
                System.out.println("No employees found in database. Creating new employees with random data...");

                EmployeeGenerator.generateRandomEmployeesByBranches(employees, branches, 100);
                EmployeeDataBaseManager.saveEmployees(employees);
                EmployeeDataBaseManager.saveBranches(branches);
                System.out.println("Successfully created and saved " + employees.size() + " employees to database.");
            } else {
                System.out.println("Reading employees from database.");
                employees = EmployeeDataBaseManager.readAllEmployees();
                System.out.println("Successfully loaded " + employees.size() + " employees from database.");
            }
        } catch (SQLException e) {
            System.out.println("Database error during employee operations: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        ScheduleFileManager.ensureScheduleFileExists(scheduleListPath, employees, employeesSchedule);
        try {
            ScheduleFileManager.employeesScheduleRead(scheduleListPath, employeesSchedule);
        } catch (IOException e) {
            System.out.println("Error reading schedule file: " + e.getMessage());
        }
        ScheduleManager.employeesScheduleSet(employees, employeesSchedule);
        employees.forEach(Employee::work);

        try {
            Map<String, Integer> stats = EmployeeQueryManager.getBranchesWithEmployeeCount();

            System.out.println("Stats by branches:");
            for (Map.Entry<String, Integer> entry : stats.entrySet()) {
                System.out.printf("Branch: %s, Employees: %d%n",
                        entry.getKey(), entry.getValue());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}




