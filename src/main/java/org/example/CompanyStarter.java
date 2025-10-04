package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
public class CompanyStarter {

    @Autowired
    private EmployeeGenerator employeeGenerator;

    @Autowired
    private BranchGenerator branchGenerator;

    public void startCompany() throws IOException, SQLException {
        String scheduleListPath = "src/main/resources/schedule.csv";
        List<Employee> employees = new ArrayList<>();
        List<Branch> branches = new ArrayList<>();
        List<Project> projects = new ArrayList<>();
        Map<Integer, List<Integer>> assignment = new HashMap<>();
        Map<Integer, List<LocalDate>> employeesSchedule = new HashMap<>();


        try {
            EmployeeDataBaseManager.initializeDatabase();

            if (!EmployeeDataBaseManager.hasEmployees()) {
                System.out.println("No employees found in database. Creating new employees with random data...");

                employeeGenerator.generateRandomEmployeesByBranches(employees, branches, 100);
                EmployeeDataBaseManager.saveEmployees(employees);
                EmployeeDataBaseManager.saveBranches(branches);
                ProjectManager.generateProjects(projects, 100);
                EmployeeDataBaseManager.saveProjects(projects);
                ProjectManager.assignProjects(employees, projects, assignment);
                EmployeeDataBaseManager.saveEmployeeProjects(assignment);

                System.out.println("Successfully created and saved " + employees.size() + " employees to database.");
            } else {
                System.out.println("Reading employees from database.");
                employees = EmployeeDataBaseManager.readAllEmployees();
                System.out.println("Successfully loaded " + employees.size() + " employees from database.");
            }
        } catch (
                SQLException e) {
            System.out.println("Database error during employee operations: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        ScheduleFileManager.ensureScheduleFileExists(scheduleListPath, employees, employeesSchedule);
        try {
            ScheduleFileManager.employeesScheduleRead(scheduleListPath, employeesSchedule);
        } catch (
                IOException e) {
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

        System.out.println("Employees of the Service Innovative Integration project:");
        List<String> employeesByProject = EmployeeQueryManager.getEmployeesByProject("Service Innovative Integration");
        employeesByProject.forEach(System.out::println);


        System.out.println("Projects of the Cathey Muller employee:");
        List<String> projectsByEmployee = EmployeeQueryManager.getProjectsByEmployee("Cathey Muller");
        projectsByEmployee.forEach(System.out::println);

        try {
            System.out.println("Projects of managers of the top 10 developers:");
            List<String> projectsForTopDevelopersManagers = EmployeeQueryManager.getProjectsForTopDevelopersManagers();
            for (String project : projectsForTopDevelopersManagers) {
                System.out.println("- " + project);

            }
        } catch (SQLException e) {
            System.out.println("Ошибка SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}