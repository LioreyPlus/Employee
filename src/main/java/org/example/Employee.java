package org.example;

import java.time.LocalDate;
import java.util.*;

public class Employee {
    private int id;
    private String name;
    private int salary;
    private String scheduleType;
    private List<Integer> schedule;
    private String title;
    private Role role;
    private Employee chief;


    Employee(int id, String name, int salary, String scheduleType, String title, Role role, Employee chief, List<Integer> schedule) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.scheduleType = scheduleType;
        this.title = title;
        this.role = role;
        this.chief = chief;
        this.schedule = schedule;
    }

    Employee(int id, String name, int salary, String scheduleType, String title, Role role, Employee chief) {
        this(id, name, salary, scheduleType, title, role, chief, null);
    }

    Employee(int id, String name) {
        this(id, name, 0, "5/2", "specialist", Role.EMPLOYEE, null);
    }

    Employee(String name, int salary, String scheduleType) {
        this(0, name, salary, scheduleType, "specialist", Role.EMPLOYEE, null);
    }

    Employee(String name, int salary) {
        this(0, name, salary, "5/2", "specialist", Role.EMPLOYEE, null);
    }

    Employee(String name) {
        this(0, name, 0, "5/2", "specialist", Role.EMPLOYEE, null);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSalary() {
        return this.salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Role getRole() { return this.role; }

    public void setRole(Role role) { this.role = role; }

    public int getId() { return id;}

    public void setId(int id) { this.id = id; }

    public Employee getChief() { return this.chief; }

    public void setChief(Employee chief) { this.chief = chief; }

    public List<Integer> getSchedule() {return this.schedule;}

    public void setSchedule(List<Integer> schedule) { this.schedule = schedule;}

    public String getScheduleType() {return  this.scheduleType;}

    public void setScheduleType(String scheduleType) {this.scheduleType = scheduleType;}



    void work() {
        int todayDayOfMonth = LocalDate.now().getDayOfMonth();
        if (schedule.contains(todayDayOfMonth)) {
            role.work(name, title, salary);
        }else {
            System.out.printf("%s doesn't work today%n", name);
        }
    }
}