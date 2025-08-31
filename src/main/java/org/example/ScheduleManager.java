package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.time.format.DateTimeFormatter;

public class ScheduleManager {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");


    public static boolean isScheduleFileCurrent(String path) throws IOException {
        try (Reader scheduleList = new FileReader(path)) {
            Iterable<CSVRecord> schedules = CSVFormat.RFC4180.parse(scheduleList);

            YearMonth currentMonth = YearMonth.now();
            LocalDate today = LocalDate.now();

            for (CSVRecord schedule : schedules) {
                for (int i = 1; i < schedule.size(); i++) {
                    String dateString = schedule.get(i);
                    if (!dateString.isBlank()) {
                        try {
                            LocalDate date = parseDate(dateString);
                            if (YearMonth.from(date).equals(currentMonth) &&
                                    !date.isBefore(today)) {
                                return true;
                            }
                        } catch (DateTimeParseException e) {
                        }
                    }
                }
            }
            return false;
        }
    }
    public static List<String> generateSchedule(String scheduleType, int daysInMonth) {
        return generateSchedule(scheduleType, YearMonth.now(), daysInMonth);
    }

    public static List<String> generateSchedule(String scheduleType, YearMonth yearMonth, int daysInMonth) {
        List<Integer> workingDays = generateWorkingDays(scheduleType, daysInMonth);
        List<String> formattedSchedule = new ArrayList<>();

        for (int day : workingDays) {
            LocalDate date = yearMonth.atDay(day);
            formattedSchedule.add(date.format(DATE_FORMATTER));
        }

        return formattedSchedule;
    }

    public static List<LocalDate> generateScheduleDates(String scheduleType, YearMonth yearMonth, int daysInMonth) {
        List<Integer> workingDays = generateWorkingDays(scheduleType, daysInMonth);
        List<LocalDate> schedule = new ArrayList<>();

        for (int day : workingDays) {
            schedule.add(yearMonth.atDay(day));
        }

        return schedule;
    }

    private static List<Integer> generateWorkingDays(String scheduleType, int daysInMonth) {
        switch (scheduleType) {
            case "5/2":
                return getWorkingDaysFor52(daysInMonth);
            case "2/2":
                return getWorkingDaysFor22(daysInMonth);
            default:
                throw new IllegalArgumentException("Unknown schedule type: " + scheduleType);
        }
    }

    private static List<Integer> getWorkingDaysFor52(int daysInMonth) {
        List<Integer> workingDays = new ArrayList<>();
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate currentDate = firstDayOfMonth.withDayOfMonth(day);
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();

            if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
                workingDays.add(day);
            }
        }

        return workingDays;
    }

    private static List<Integer> getWorkingDaysFor22(int daysInMonth) {
        List<Integer> workingDays = new ArrayList<>();
        Random random = new Random();
        int startDay = random.nextBoolean() ? 1 : 2;
        boolean isWorkDay = true;

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

    public static void employeesScheduleSet(List<Employee> employees, Map<Integer, List<LocalDate>> employeesSchedule) {
        for (Employee employee : employees) {
            employee.setSchedule(employeesSchedule.get(employee.getId()));
        }
    }

    public static String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    public static LocalDate parseDate(String dateString) {
        return LocalDate.parse(dateString, DATE_FORMATTER);
    }
}