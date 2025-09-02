package org.example;

import com.github.javafaker.Faker;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class EmployeeGenerator {

    public static void generateEmployeeList(String employeeListPath, List<Employee> employees) throws IOException {
        Random random = new Random();
        int employeeCount = 57 + random.nextInt(5);  // 57–61
        int developerCount = 27 + random.nextInt(5); // 27–31
        int managerCount = 100 - employeeCount - developerCount - 1; // 7 - 15

        generateEmployees(employees, 1, Role.DIRECTOR);
        generateEmployees(employees, managerCount, Role.MANAGER);
        generateEmployees(employees, developerCount, Role.DEVELOPER);
        generateEmployees(employees, employeeCount, Role.EMPLOYEE);

        EmployeeFileManager.employeeCSVBuild(employeeListPath, employees);
    }

    private static void generateEmployees(List<Employee> employees, int count, Role role) {
        List<Employee> potentialChiefs = employees.stream()
                .filter(e -> e.getRole() == role.getRequiredChiefRole())
                .toList();

        for (int i = 0; i < count; i++) {
            Employee chief = null;
            if (!potentialChiefs.isEmpty()) {
                chief = potentialChiefs.get(new Random().nextInt(potentialChiefs.size()));
            }
            int id = generateUniqueId(employees);
            String name = new Faker().name().fullName();
            Employee employee = new Employee(id, name);
            employee.setRole(role);
            employee.setChief(chief);
            employee.setSalary(role.generateRandomSalary());
            employee.setScheduleType(role.getScheduleType());
            employee.setTitle(role.getRandomTitle());
            employees.add(employee);
        }
    }

    private static int generateUniqueId(List<Employee> employees) {
        return employees.stream()
                .mapToInt(Employee::getId)
                .max()
                .orElse(0) + 1;
    }
}