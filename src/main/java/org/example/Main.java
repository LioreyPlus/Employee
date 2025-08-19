package org.example;

import com.github.javafaker.Faker;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.*;

import java.time.*;


public class Main {
    public static void main(String[] args) throws IOException {
        String employeeListPath = "../EmployeeList.csv";
        String scheduleListPath = "../schedule.csv";
        List<Employee> employees = new ArrayList<>();
        Map<Integer, List<Integer>> employeesSchedule = new HashMap<>();

        try {
            employeeListRead(employeeListPath, employees);
        } catch (FileNotFoundException e) {
            System.out.printf("We didn't find a file for employees to read.%nCreating a new file with random employees.%n");
            employeeListCreate(employeeListPath, employees);
            System.out.println("The file has been successfully created and added to the list.");
        }
        try {
            employeesScheduleRead(scheduleListPath, employeesSchedule);
        } catch (FileNotFoundException t) {
            System.out.printf("We didn't find a file for employee's schedule to read.%nCreating a new file with random schedule.%n");
            ScheduleCreate(scheduleListPath, employees);
            System.out.println("The file has been successfully created and added to the list.");
        }
        employeesScheduleSet(employees, employeesSchedule);
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
            String scheduleType = worker.get(4);
            String title = worker.get(5);
            tempEmployees.add(new Employee(id, name, salary, scheduleType, title, role, null));
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
                        .orElseThrow(() -> new IllegalStateException("Chief not found: " + chiefId)));
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
            setRandomScheduleType(employee);
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
                    var chiefId = employee.getChief() != null ? employee.getChief().getId() : " ";
                    try {
                        printer.printRecord(employee.getId(), role, employee.getName(), salary, employee.getScheduleType(), employee.getTitle(), chiefId);
                        printer.flush();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }


    private static Role getrequiredChiefRole(Role role) {
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


    private static void setRandomScheduleType(Employee employee) {
        Random random = new Random();
        String scheduleType = random.nextBoolean() ? "5/2" : "2/2";
        switch (employee.getRole()) {
            case DIRECTOR, MANAGER, DEVELOPER -> employee.setScheduleType("5/2");
            case EMPLOYEE -> employee.setScheduleType(scheduleType);
        }
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
            case DIRECTOR -> {
                List<String> titles = Arrays.asList("CEO");
                employee.setTitle(titles.get(random.nextInt(titles.size())));
            }
            default -> {
                List<String> titles = Arrays.asList("specialist", "laboratory assistant", "senior specialist", "leading specialist", "expert");
                employee.setTitle(titles.get(random.nextInt(titles.size())));
            }

        }
    }

    private static void employeesScheduleRead(String scheduleListPath, Map<Integer, List<Integer>> employeesSchedule) throws IOException {
        Reader scheduleList = new FileReader(scheduleListPath);
        Iterable<CSVRecord> schedules = CSVFormat.RFC4180.parse(scheduleList);

        for (CSVRecord schedule : schedules) {
            List<Integer> weekends = new ArrayList<>();
            int workerID = Integer.parseInt(schedule.get(0));
            for (int i = 1; i < schedule.size(); i++) {
                if (!schedule.get(i).isBlank()) {
                    weekends.add(Integer.parseInt(schedule.get(i)));
                }
            }
            employeesSchedule.put(workerID, weekends);

        }
        scheduleList.close();
    }

    private static void employeesScheduleSet(List<Employee> employees, Map<Integer, List<Integer>> employeesSchedule) {
        for (Employee employee : employees) {
            employee.setSchedule(employeesSchedule.get(employee.getId()));
        }
    }

    private static void ScheduleCreate(String scheduleListPath, List<Employee> employees) throws IOException {
        Writer writer = new FileWriter(scheduleListPath);
        CSVPrinter printer = CSVFormat.DEFAULT.builder()
                .build()
                .print(writer);

        YearMonth currentMonth = YearMonth.now();
        int daysInMonth = currentMonth.lengthOfMonth();

        for (Employee employee : employees) {
            List<String> scheduleRecord = new ArrayList<>();
            scheduleRecord.add(String.valueOf(employee.getId()));
            List<Integer> workingDays = generateWorkingDays(employee.getScheduleType(), daysInMonth);
            for (int day = 1; day <= daysInMonth; day++) {
                if (workingDays.contains(day)) {
                    scheduleRecord.add(String.valueOf(day));
                } else {
                    scheduleRecord.add("");
                }
            }
            printer.printRecord(scheduleRecord);
            printer.flush();
        }
}

    private static List<Integer> generateWorkingDays(String scheduleType, int daysInMonth) {
        List<Integer> workingDays = new ArrayList<>();

        switch (scheduleType) {
            case "5/2" -> workingDays.addAll(getWorkingDaysFor52(daysInMonth));
            case "2/2" -> workingDays.addAll(getWorkingDaysFor22(daysInMonth));
            default ->  throw new IllegalArgumentException("Неизвестный тип графика: " + scheduleType);
        }

        return workingDays;
    }
    private static List<Integer> getWorkingDaysFor52(int daysInMonth) {
        List<Integer> workingDays = new ArrayList<>();
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate currentDate = firstDayOfMonth.withDayOfMonth(day);
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();

            if (dayOfWeek.getValue() >= DayOfWeek.MONDAY.getValue() &&
                    dayOfWeek.getValue() <= DayOfWeek.FRIDAY.getValue()) {
                workingDays.add(day);
            }
        }

        return workingDays;
    }
    private static List<Integer> getWorkingDaysFor22(int daysInMonth) {
        List<Integer> workingDays = new ArrayList<>();

        Random random = new Random();
        int startDay = random.nextBoolean() ? 1 : 3;
        boolean startWithWork = true;

        boolean isWorkDay = startWithWork;

        for (int day = startDay; day <= daysInMonth; day++) {
            if (isWorkDay) {
                workingDays.add(day);
            }

            if ((day - startDay + 1) % 2 == 0) {
                isWorkDay = !isWorkDay;
            }
        }

        return workingDays;
    }
}



