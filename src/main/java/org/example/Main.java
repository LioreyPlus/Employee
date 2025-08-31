package org.example;

import com.github.javafaker.Faker;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import java.time.*;


public class Main {
    public static void main(String[] args) throws IOException {
        String employeeListPath = "../EmployeeList.csv";
        String scheduleListPath = "../schedule.csv";
        List<Employee> employees = new ArrayList<>();
        Map<Integer, List<LocalDate>> employeesSchedule = new HashMap<>();


        File employeeListFile = new File(employeeListPath);
        if (!employeeListFile.exists()) {
            System.out.printf("We didn't find a file for employees to read.%nCreating a new file with random employees.%n");
            EmployeeGenerator.generateEmployeeList(employeeListPath, employees);
            System.out.println("The file has been successfully created and added to the list.");
        }
        try {
            EmployeeFileManager.employeeListRead(employeeListPath, employees);
        } catch (IOException e) {
            System.out.println("Error reading employee's list file: " + e.getMessage());
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




