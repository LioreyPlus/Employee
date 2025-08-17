package org.example;

import com.github.javafaker.Faker;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String employeeListPath = "../EmployeeList.csv";
        List<Employee> employees = new ArrayList<>();

        try {
            employeeListRead(employeeListPath, employees);
        } catch (FileNotFoundException e) {
            System.out.printf("We didn't find a file for employees to read.%nCreating a new file with random employees.%n");
            employeeListCreate(employeeListPath, employees);
            System.out.println("The file has been successfully created and added to the list.");
        }

        employees.forEach(Employee::work);
    }

    private static void employeeListRead(String employeeListPath, List<Employee> employees) throws IOException {
        List<Employee> tempEmployees = new ArrayList<>();
        Map<Integer, Integer> chiefMap = new HashMap<>();

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

            tempEmployees.add(new Employee(id, name, salary, weekends, title, role, null));
            if (!worker.get(6).isBlank()) {
                chiefMap.put(id, Integer.parseInt(worker.get(6)));
            }


        }

        for (Employee employee : tempEmployees) {
            if (chiefMap.containsKey(employee.getId())) {
                int chiefId = chiefMap.get(employee.getId());
                employee.setChief(tempEmployees.stream()
                        .filter(e -> e.getId() == chiefId)
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Supervisor not found: " + chiefId)));
            }
        }
        employees.addAll(tempEmployees);
        workersList.close();
    }

    public static void employeeListCreate(String employeeListPath, List<Employee> employees) throws IOException {
        Random random = new Random();
        int employeeCount = 57 + random.nextInt(5);  // 57–61
        int developerCount = 27 + random.nextInt(5); // 27–31
        int managerCount = 100 - employeeCount - developerCount - 1; // 7 - 15

        generateEmployees(employees, 1, Role.DIRECTOR);
        generateEmployees(employees, managerCount, Role.MANAGER);
        generateEmployees(employees, developerCount, Role.DEVELOPER);
        generateEmployees(employees, employeeCount, Role.EMPLOYEE);

        employeeCSVBuild(employeeListPath, employees);

    }

    private static void generateEmployees(List<Employee> employees, int count, Role role) {

        List<Employee> potentialChiefs = employees.stream()
                .filter(e -> e.getRole() == getrequiredChiefRole(role))
                .toList();
        for (int i = 0; i < count; i++) {
            Employee chief = null;
            if (!potentialChiefs.isEmpty()) {
                chief = potentialChiefs.get(new Random().nextInt(potentialChiefs.size()));
            }
            int id = generateUniqueId(employees);
            String name = new Faker().name().fullName();
            Employee employee;
            employee = new Employee(id, name);
            employee.setRole(role);
            employee.setChief(chief);
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
                    var chiefId = employee.getChief() != null ? employee.getChief().getId() : " ";
                    try {
                        printer.printRecord(employee.getId(), role, employee.getName(), salary, weekends, employee.getTitle(), chiefId);
                        printer.flush();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }


    private static Role getrequiredChiefRole(Role role){
        return switch (role) {
            case EMPLOYEE, DEVELOPER -> Role.MANAGER;
            case MANAGER -> Role.DIRECTOR;
            default -> null;
        };
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

            case DEVELOPER -> employee.setSalary(120000 + random.nextInt(200000));
            case MANAGER -> employee.setSalary(80000 + random.nextInt(60000));
            case DIRECTOR -> employee.setSalary(300000 + random.nextInt(200000));
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

            case DEVELOPER -> {
                List<String> titles = Arrays.asList("junior", "middle", "senior");
                employee.setTitle(titles.get(random.nextInt(titles.size())));
            }
            case MANAGER -> {
                List<String> titles = Arrays.asList("efficiency manager", "supervisor", "group leader", "team leader");
                employee.setTitle(titles.get(random.nextInt(titles.size())));
            }
            case DIRECTOR ->  {
                List<String> titles = Arrays.asList("CEO");
                employee.setTitle(titles.get(random.nextInt(titles.size())));
            }
            default -> {
                List<String> titles = Arrays.asList("specialist", "laboratory assistant", "senior specialist", "leading specialist", "expert");
                employee.setTitle(titles.get(random.nextInt(titles.size())));
            }

        }
    }
}




