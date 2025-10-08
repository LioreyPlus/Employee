package org.example;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class EmployeeGenerator {

    private final Faker faker;
    private final Random random;
    private final BranchGenerator branchGenerator;

    @Autowired
    public EmployeeGenerator(Faker faker, Random random, BranchGenerator branchGenerator) {
        this.faker = faker;
        this.random = random;
        this.branchGenerator = branchGenerator;
    }

    public void generateRandomEmployeesByBranches(List<Employee> employees, List<Branch> branches, int count) {

        int employeeCount = 57 + random.nextInt(5);
        int developerCount = 27 + random.nextInt(5);
        int branchManagerCount = count / 50;
        int managerCount = count - employeeCount - developerCount - 1 - branchManagerCount;

        branches.addAll(branchGenerator.createRandomBranches(branchManagerCount));

        generateEmployees(employees, 1, Role.DIRECTOR);
        generateEmployees(employees, branchManagerCount, Role.BRANCH_MANAGER);
        generateEmployees(employees, managerCount, Role.MANAGER);
        generateEmployees(employees, developerCount, Role.DEVELOPER);
        generateEmployees(employees, employeeCount, Role.EMPLOYEE);
    }

    private void generateEmployees(List<Employee> employees, int count, Role role) {
        List<Employee> potentialChiefs = employees.stream()
                .filter(e -> e.getRole() == role.getRequiredChiefRole())
                .toList();

        for (int i = 0; i < count; i++) {
            Employee chief = null;
            if (!potentialChiefs.isEmpty()) {
                chief = potentialChiefs.get(random.nextInt(potentialChiefs.size()));
            }

            int id = generateUniqueId(employees);
            String name = faker.name().fullName();
            Employee employee = Employee.builder()
                    .id(id)
                    .name(name)
                    .salary(role.generateRandomSalary())
                    .scheduleType(role.getScheduleType())
                    .title(role.getRandomTitle())
                    .role(role)
                    .chief(chief)
                    .branchId(setCorrectBranchId(role, chief, id))
                    .build();

            employees.add(employee);
        }
    }

    private int generateUniqueId(List<Employee> employees) {
        return employees.stream()
                .mapToInt(Employee::getId)
                .max()
                .orElse(0) + 1;
    }

    private int setCorrectBranchId(Role role, Employee chief, int employeeId) {
        switch (role) {
            case BRANCH_MANAGER -> {
                return employeeId - 1;
            }
            case MANAGER, EMPLOYEE, DEVELOPER -> {
                return chief != null ? chief.getBranchId() : 0;
            }
            default -> {
                return 0;
            }
        }
    }
}