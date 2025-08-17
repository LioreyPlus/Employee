package org.example;

import java.util.*;

public class Employee {
    private int id;
    private String name;
    private int salary;
    private List<DayOfWeek> weekend;
    private String title;
    private Role role;
    private Employee chief;


    Employee(int id, String name, int salary, List<DayOfWeek> weekend, String title, Role role, Employee chief) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.weekend = weekend;
        this.title = title;
        this.role = role;
        this.chief = chief;
    }

    Employee(int id, String name) {
        this(id, name, 0, List.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY), "специалист", Role.EMPLOYEE, null);
    }

    Employee(String name, int salary, List<DayOfWeek> weekend) {
        this(0, name, salary, weekend, "cпециалист", Role.EMPLOYEE, null);
    }

    Employee(String name, int salary) {
        this(0, name, salary, List.of(DayOfWeek.SUNDAY, DayOfWeek.SATURDAY), "cпециалист", Role.EMPLOYEE, null);
    }

    Employee(String name) {
        this(0, name, 0, List.of(DayOfWeek.SUNDAY, DayOfWeek.SATURDAY), "специалист", Role.EMPLOYEE, null);
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

    public List<DayOfWeek> getWeekend() {
        return this.weekend;
    }

    public void setWeekend(List<DayOfWeek> weekend) {
        this.weekend = weekend;
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

    boolean isWork(DayOfWeek dow) {
        if (weekend.contains(dow)) {
            System.out.printf("%s не работает сегодня%n", name);
            return false;
        } else {
            System.out.printf("%s сегодня работает. Ура!%n", name);
            return true;
        }

    }

    void work() {
        role.work(name, title, salary);
    }
}