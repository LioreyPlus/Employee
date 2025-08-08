package org.example;

import com.github.javafaker.Faker;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Random random = new Random();
        List<Employee> employees = new ArrayList<>();

        try {
            Reader workersList = new FileReader("C:\\Users\\DeaDFlame\\Desktop\\Employee\\EmployeeList.csv");
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
        } catch (FileNotFoundException e) {
            System.out.printf("Не нашёлся файл для считывания сотрудников.%nСоздаём новый файл со случайными сотрудниками");
            new PrintWriter(new File("C:\\Users\\DeaDFlame\\Desktop\\Employee\\EmployeeList.csv"));
            int employeeCount = 58 + random.nextInt(5);  // 58–62
            int developerCount = 28 + random.nextInt(5); // 28–32
            int managerCount = 10 + random.nextInt(3);

            generateEmployees(employees, employeeCount, "Employee");
            generateEmployees(employees, developerCount, "Developer");
            generateEmployees(employees, managerCount, "Manager");

            Writer writer = new FileWriter("C:\\Users\\DeaDFlame\\Desktop\\Employee\\EmployeeList.scv");
            CSVPrinter printer = CSVFormat.DEFAULT.builder()
                    .setHeader("Тип", "Имя", "Зарплата", "Выходные", "Должность")
                    .build()
                    .print(writer);

            employees.stream()
                    .filter(p -> p instanceof Developer)
                    .forEach(employee -> {
                        String name = employee.getName();
                        String salary = Integer.toString(employee.getSalary());
                        String weekends = employee.getWeekend().toString();
                        String title = employee.getTitle();
                        try {
                            printer.printRecord("Developer", name, salary, weekends, title);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                    });
            employees.stream()
                    .filter(p -> p instanceof Manager)
                    .forEach(employee -> {
                        String name = employee.getName();
                        String salary = Integer.toString(employee.getSalary());
                        String weekends = employee.getWeekend().toString();
                        String title = employee.getTitle();
                        try {
                            printer.printRecord("Manager", name, salary, weekends, title);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                    });

            employees.stream()
                    .filter(p -> p.getClass() == Employee.class)
                    .forEach(employee -> {
                        String name = employee.getName();
                        String salary = Integer.toString(employee.getSalary());
                        String weekends = employee.getWeekend().toString();
                        String title = employee.getTitle();
                        try {
                            printer.printRecord("Employee", name, salary, weekends, title);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                    });


        }


        List<Employee> orderedEmployee = new ArrayList<>();
        employees.stream()
                .filter(e -> e instanceof Developer)
                .forEach(orderedEmployee::add);

        employees.stream()
                .filter(e -> e instanceof Manager)
                .forEach(orderedEmployee::add);

        employees.stream()
                .filter(e -> e.getClass() == Employee.class)
                .forEach(orderedEmployee::add);

        orderedEmployee.forEach(Employee::work);
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
            employee.setRandomSalary();
            employee.setRandomWeekend();
            employee.setRandomTitle();
            employees.add(employee);
        }

    }
}




