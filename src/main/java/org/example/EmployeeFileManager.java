package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EmployeeFileManager {

    public static void employeeListRead(String employeeListPath, List<Employee> employees) throws IOException {
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
            tempEmployees.add(Employee.builder()
                    .id(id)
                    .name(name)
                    .salary(salary)
                    .scheduleType(scheduleType)
                    .title(title)
                    .role(role)
                    .build());
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

    public static void employeeCSVBuild(String employeeListPath, List<Employee> employees) throws IOException {
        Writer writer = new FileWriter(employeeListPath);
        CSVPrinter printer = CSVFormat.DEFAULT.builder()
                .build()
                .print(writer);

        employees.forEach(employee -> {
            String role = employee.getRole().toString()
                    .replaceAll("[\\[\\],]", "")
                    .trim();
            String salary = Integer.toString(employee.getSalary());
            var chiefId = employee.getChief() != null ? employee.getChief().getId() : " ";
            try {
                printer.printRecord(employee.getId(), role, employee.getName(), salary,
                        employee.getScheduleType(), employee.getTitle(), chiefId);
                printer.flush();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
