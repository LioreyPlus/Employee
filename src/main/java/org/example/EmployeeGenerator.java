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

        int employeeCount = 57 + new Random().nextInt(5);
        int developerCount = 27 + new Random().nextInt(5);
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

        Random random = new Random();
        Faker faker = new Faker();

        for (int i = 0; i < count; i++) {
            Employee chief = null;
            if (!potentialChiefs.isEmpty()) {
                chief = potentialChiefs.get(random.nextInt(potentialChiefs.size()));
            }

            int id = generateUniqueId(employees);
            String name = faker.name().fullName();

            Employee employee = new Employee(id, name);
            employee.setRole(role);
            employee.setChief(chief);
            employee.setSalary(role.generateRandomSalary());
            employee.setScheduleType(role.getScheduleType());
            employee.setTitle(role.getRandomTitle());
            employee.setBranchId(setCorrectBranchId(employee));

            employees.add(employee);
        }
    }

    private int generateUniqueId(List<Employee> employees) {
        return employees.stream()
                .mapToInt(Employee::getId)
                .max()
                .orElse(0) + 1;
    }

    private int setCorrectBranchId(Employee employee) {
        switch (employee.getRole()) {
            case BRANCH_MANAGER -> {return employee.getId() - 1;}
            case MANAGER, EMPLOYEE, DEVELOPER -> {return employee.getChief().getBranchId();}
            default -> {return 0;}
            }
    }
}