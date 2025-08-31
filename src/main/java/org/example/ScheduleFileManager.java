package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScheduleFileManager {

    public static void ensureScheduleFileExists(String path, List<Employee> employees,
                                                Map<Integer, List<LocalDate>> schedule) throws IOException {
        File file = new File(path);
        boolean needToCreate = !file.exists();

        if (!needToCreate) {
            needToCreate = !ScheduleManager.isScheduleFileCurrent(path);
        }

        if (needToCreate) {
            System.out.printf("Schedule file not found or outdated. Creating new file.%n");
            scheduleCreate(path, employees);
            System.out.println("Schedule file created successfully.");
        }
    }

    private static void scheduleCreate(String scheduleListPath, List<Employee> employees) throws IOException {
        Writer writer = new FileWriter(scheduleListPath);
        CSVPrinter printer = CSVFormat.DEFAULT.builder()
                .build()
                .print(writer);
        YearMonth currentMonth = YearMonth.now();
        LocalDate today = LocalDate.now();
        int daysRemaining = currentMonth.lengthOfMonth() - today.getDayOfMonth();
        for (Employee employee : employees) {
            List<String> scheduleRecord = new ArrayList<>();
            scheduleRecord.add(String.valueOf(employee.getId()));

            List<String> currentMonthSchedule = ScheduleManager.generateSchedule(
                    employee.getScheduleType(),
                    currentMonth,
                    currentMonth.lengthOfMonth()
            );
            for (String workDate : currentMonthSchedule) {
                LocalDate date = ScheduleManager.parseDate(workDate);
                if (!date.isBefore(today)) {
                    scheduleRecord.add(workDate);
                }
            }
            if (daysRemaining < 20) {
                YearMonth nextMonth = currentMonth.plusMonths(1);
                List<String> nextMonthSchedule = ScheduleManager.generateSchedule(
                        employee.getScheduleType(),
                        nextMonth,
                        nextMonth.lengthOfMonth()
                );

                scheduleRecord.addAll(nextMonthSchedule);
            }

            printer.printRecord(scheduleRecord);
            printer.flush();
        }
        writer.close();
    }

    public static void employeesScheduleRead(String scheduleListPath, Map<Integer, List<LocalDate>> employeesSchedule) throws IOException {
        Reader scheduleList = new FileReader(scheduleListPath);
        Iterable<CSVRecord> schedules = CSVFormat.RFC4180.parse(scheduleList);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        for (CSVRecord schedule : schedules) {
            List<LocalDate> workDates = new ArrayList<>();
            int workerID = Integer.parseInt(schedule.get(0));

            for (int i = 1; i < schedule.size(); i++) {
                String dateString = schedule.get(i);
                if (!dateString.isBlank()) {
                    try {
                        LocalDate workDate = LocalDate.parse(dateString, formatter);
                        workDates.add(workDate);
                    } catch (DateTimeParseException e) {
                        System.out.println("Incorrect date format. Employee ID " + workerID);
                    }
                }
            }
            employeesSchedule.put(workerID, workDates);
        }
        scheduleList.close();
    }
}