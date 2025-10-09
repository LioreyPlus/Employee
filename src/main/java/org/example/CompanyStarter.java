package org.example;

import org.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
public class CompanyStarter implements CommandLineRunner {

    @Autowired
    private EmployeeGenerator employeeGenerator;

    @Autowired
    private BranchGenerator branchGenerator;

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private EmployeeCrudRepository employeeCrudRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Override
    public void run(String... args) throws Exception {
        startCompany();
    }

    public void startCompany() throws Exception {
        String scheduleListPath = "src/main/resources/schedule.csv";
        List<Employee> employees = new ArrayList<>();
        List<Branch> branches = new ArrayList<>();
        List<Project> projects = new ArrayList<>();
        Map<Integer, List<Integer>> assignment = new HashMap<>();
        Map<Integer, List<LocalDate>> employeesSchedule = new HashMap<>();

        try {
            databaseInitializer.initializeDatabase();

            if (!databaseInitializer.hasEmployees()) {
                System.out.println("No employees found in database. Creating new employees with random data...");

                employeeGenerator.generateRandomEmployeesByBranches(employees, branches, 100);
                employeeCrudRepository.saveAll(employees);
                branchRepository.saveAll(branches);

                ProjectManager.generateProjects(projects, 100);
                projectRepository.saveAll(projects);

                ProjectManager.assignProjects(employees, projects, assignment);
                projectRepository.saveEmployeeProjects(assignment);

                System.out.println("Successfully created and saved " + employees.size() + " employees to database.");
            } else {
                System.out.println("Reading employees from database.");
                employees = employeeCrudRepository.findAll(); // ✅ Используем EmployeeCrudRepository
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
            Map<String, Integer> stats = statisticsRepository.getBranchesWithEmployeeCount();
            System.out.println("Stats by branches:");
            for (Map.Entry<String, Integer> entry : stats.entrySet()) {
                System.out.printf("Branch: %s, Employees: %d%n", entry.getKey(), entry.getValue());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Employees of the Service Innovative Integration project:");
        List<String> employeesByProject = statisticsRepository.findEmployeesByProject("Service Innovative Integration");
        employeesByProject.forEach(System.out::println);

        System.out.println("Projects of the Cathey Muller employee:");
        List<String> projectsByEmployee = statisticsRepository.findProjectsByEmployee("Cathey Muller");
        projectsByEmployee.forEach(System.out::println);

        try {
            System.out.println("Projects of managers of the top 10 developers:");
            List<String> projectsForTopDevelopersManagers = statisticsRepository.findProjectsForTopDevelopersManagers();
            for (String project : projectsForTopDevelopersManagers) {
                System.out.println("- " + project);
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}