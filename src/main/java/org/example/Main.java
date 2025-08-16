package org.example;

import com.github.javafaker.Faker;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {
        String employeeListPath = "../EmployeeList.csv";
        List<Employee> employees = new ArrayList<>();

        try {
            employeeListRead(employeeListPath, employees);
        } catch (FileNotFoundException e) {
            System.out.printf("We did't find a file for employees to read.%nCreating a new file with random employees.%n");
            employeeListCreate(employeeListPath, employees);
            System.out.println("The file has been successfully created and added to the list.");
        }

        employees.forEach(Employee::work);
    }

    private static void employeeListRead(String employeeListPath, List<Employee> employees) throws IOException {
        Reader workersList = new FileReader(employeeListPath);
        Iterable<CSVRecord> workers = CSVFormat.RFC4180.parse(workersList);

        for (CSVRecord worker : workers) {
            int id = Integer.parseInt(worker.get(0));
            Role role = Role.valueOf(worker.get(1).toUpperCase(Locale.ROOT));
            String name = worker.get(2);
            int salary = Integer.parseInt(worker.get(3));
            List<DayOfWeek> weekends = Arrays.stream(worker.get(4).split("\\s+"))
                    .map(String::toUpperCase)
                    .map(DayOfWeek::valueOf)
                    .toList();
            String title = worker.get(5);
            employees.add(new Employee(id, name, salary, weekends, title, role));

        }
        workersList.close();
    }

    public static void employeeListCreate(String employeeListPath, List<Employee> employees) throws IOException {
        Random random = new Random();
        int employeeCount = 57 + random.nextInt(5);  // 57–61
        int developerCount = 27 + random.nextInt(5); // 27–31
        int managerCount = 100 - employeeCount - developerCount; // 8 - 16

        generateEmployees(employees, employeeCount, Role.EMPLOYEE);
        generateEmployees(employees, developerCount, Role.DEVELOPER);
        generateEmployees(employees, managerCount, Role.MANAGER);

        employeeCSVBuild(employeeListPath, employees);

    }

    private static void generateEmployees(List<Employee> employees, int count, Role role) {
        for (int i = 0; i < count; i++) {
            int id = generateUniqueId(employees);
            String name = new Faker().name().fullName();
            Employee employee;
            employee = new Employee(id, name);
            employee.setRole(role);
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
                    String role = employee.getRole().toString()
                            .replaceAll("[\\[\\],]", "")
                            .trim();
                    String salary = Integer.toString(employee.getSalary());
                    String weekends = employee.getWeekend().toString()
                            .replaceAll("[\\[\\],]", "")
                            .trim();
                    try {
                        printer.printRecord(employee.getId(), role, employee.getName(), salary, weekends, employee.getTitle());
                        printer.flush();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    private static int generateUniqueId(List<Employee> employees) {
        return employees.stream()
                .mapToInt(Employee::getId)
                .max()
                .orElse(0) + 1;
    }

    private static void setRandomSalary(Employee employee) {
        Random random = new Random();

        switch (employee.getRole()) {

            case Role.DEVELOPER -> employee.setSalary(120000 + random.nextInt(200000));
            case Role.MANAGER -> employee.setSalary(80000 + random.nextInt(60000));
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
        switch (employee.getRole()) {

            case Role.DEVELOPER -> {
                List<String> titles = Arrays.asList("junior", "middle", "senior");
                employee.setTitle(titles.get(random.nextInt(titles.size())));
            }
            case Role.MANAGER -> {
                List<String> titles = Arrays.asList("efficiency manager", "supervisor", "group leader", "team leader");
                employee.setTitle(titles.get(random.nextInt(titles.size())));
            }
            default -> {
                List<String> titles = Arrays.asList("specialist", "laboratory assistant", "senior specialist", "leading specialist", "expert");
                employee.setTitle(titles.get(random.nextInt(titles.size())));
            }

        }
    }
}




