package org.example;

import com.github.javafaker.Faker;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import java.time.*;


public class Main {
    public static void main(String[] args) throws IOException {
        String scheduleListPath = "src/main/resources/schedule.scv";
        List<Employee> employees = new ArrayList<>();
        Map<Integer, List<LocalDate>> employeesSchedule = new HashMap<>();


        try {
            EmployeeDataBaseManager.initiazlizeDatabase();

            if (!EmployeeDataBaseManager.hasEmployees()) {
                System.out.println("No employees found in database. Creating new employees with random data...");

                EmployeeGenerator.generateRandomEmployees(employees, 100);
                EmployeeDataBaseManager.saveEmployees(employees);
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
    }
}




