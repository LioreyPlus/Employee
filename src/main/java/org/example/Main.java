package org.example;

import com.github.javafaker.Faker;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String employeeListPath = "C:\\Users\\DeaDFlame\\Desktop\\Employee\\EmployeeList.csv";
        List<Employee> employees = new ArrayList<>();

        try {
            employeeListRead(employeeListPath, employees);
        } catch (FileNotFoundException e) {
            System.out.printf("Не нашёлся файл для считывания сотрудников.%nСоздаём новый файл со случайными сотрудниками.%n");
            employeeListCreate(employeeListPath, employees);
            System.out.println("Файл успешно создан и записан в список.");
        }

        employees.forEach(Employee::work);
    }

    private static void employeeListRead(String employeeListPath, List<Employee> employees) throws IOException {
        Reader workersList = new FileReader(employeeListPath);
        Iterable<CSVRecord> workers = CSVFormat.RFC4180.parse(workersList);

        for (CSVRecord worker : workers) {
            String name = worker.get(1);
            int salary = Integer.parseInt(worker.get(2));
            List<DayOfWeek> weekends = Arrays.stream(worker.get(3).split("\\s+"))
                    .map(String::toUpperCase)
                    .map(DayOfWeek::valueOf)
                    .toList();
            String title = worker.get(4);

            switch (worker.get(0)) {
                case "Employee" -> employees.add(new Employee(name, salary, weekends));
                case "Manager" -> employees.add(new Manager(name, salary, weekends, title));
                case "Developer" -> employees.add(new Developer(name, salary, weekends, title));
                default -> System.out.println("Мы не смогли распознать сотрудника");
            }
        }
        workersList.close();
    }

    public static void employeeListCreate(String employeeListPath, List<Employee> employees) throws IOException {
        Random random = new Random();
        int employeeCount = 57 + random.nextInt(5);  // 57–61
        int developerCount = 27 + random.nextInt(5); // 27–31
        int managerCount = 100 - employeeCount - developerCount; // 8 - 16

        generateEmployees(employees, employeeCount, "Employee");
        generateEmployees(employees, developerCount, "Developer");
        generateEmployees(employees, managerCount, "Manager");

        employeeCSVBuild(employeeListPath, employees);

    }

    private static void generateEmployees(List<Employee> employees, int count, String employeeType) {
        for (int i = 0; i < count; i++) {
            String name = new Faker().name().fullName();
            Employee employee;
            switch (employeeType) {
                case "Manager" -> employee = new Manager(name);
                case "Developer" -> employee = new Developer(name);
                default -> employee = new Employee(name);
            }
            setRandomSalary(employee);
            setRandomWeekend(employee);
            setRandomTitle(employee);
            employees.add(employee);
        }
    }

    public static void employeeCSVBuild(String employeeListPath, List<Employee> employees) throws IOException {
        Writer writer = new FileWriter(employeeListPath);
        CSVPrinter printer = CSVFormat.DEFAULT.builder()
                .build()
                .print(writer);

        employees
                .forEach(employee -> {
                    String type = employee.getType();
                    String name = employee.getName();
                    String salary = Integer.toString(employee.getSalary());
                    String weekends = employee.getWeekend().toString();
                    String weekendsForReader = weekends
                            .replaceAll("[\\[\\],]", "")
                            .trim();
                    String title = employee.getTitle();
                    try {
                        printer.printRecord(type, name, salary, weekendsForReader, title);
                        printer.flush();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    private static void setRandomSalary(Employee employee) {
        Random random = new Random();
        switch (employee.getType()) {

            case "Developer" -> employee.setSalary(120000 + random.nextInt(200000));
            case "Manager" -> employee.setSalary(80000 + random.nextInt(60000));
            default -> employee.setSalary(40000 + random.nextInt(50000));
        }
    }

    private static void setRandomWeekend(Employee employee) {
        Random random = new Random();
        List<DayOfWeek> allDays = Arrays.asList(DayOfWeek.values());
        Collections.shuffle(allDays);
        employee.setWeekend(allDays.subList(0, random.nextInt(3) + 1));
    }

    private static void setRandomTitle(Employee employee) {
        Random random = new Random();
        switch (employee.getType()) {

            case "Developer" -> {
                List<String> titles = Arrays.asList("джун", "мидл", "сеньор");
                employee.setTitle(titles.get(random.nextInt(titles.size())));
            }
            case "Manager" -> {
                List<String> titles = Arrays.asList("менеджер по эфффективности", "супервайзер", "руководитель группы", "тимлид");
                employee.setTitle(titles.get(random.nextInt(titles.size())));
            }
            default -> {
                List<String> titles = Arrays.asList("специалист", "лаборант", "старший специалист", "ведущий специалист", "эксперт");
                employee.setTitle(titles.get(random.nextInt(titles.size())));
            }

        }
    }
}




