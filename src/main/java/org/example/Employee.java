package org.example;

import java.time.LocalDate;
import java.util.List;

public class Employee {
    private int id;
    private String name;
    private int salary;
    private String scheduleType;
    private List<LocalDate> schedule;
    private String title;
    private Role role;
    private Employee chief;
    private int branchId;

    // Приватный конструктор для Builder
    private Employee(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.salary = builder.salary;
        this.scheduleType = builder.scheduleType;
        this.schedule = builder.schedule;
        this.title = builder.title;
        this.role = builder.role;
        this.chief = builder.chief;
        this.branchId = builder.branchId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int id;
        private String name;
        private int salary;
        private String scheduleType = "5/2"; // значение по умолчанию
        private List<LocalDate> schedule;
        private String title = "specialist"; // значение по умолчанию
        private Role role = Role.EMPLOYEE;   // значение по умолчанию
        private Employee chief;
        private int branchId;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder salary(int salary) {
            this.salary = salary;
            return this;
        }

        public Builder scheduleType(String scheduleType) {
            this.scheduleType = scheduleType;
            return this;
        }

        public Builder schedule(List<LocalDate> schedule) {
            this.schedule = schedule;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Builder chief(Employee chief) {
            this.chief = chief;
            return this;
        }

        public Builder branchId(int branchId) {
            this.branchId = branchId;
            return this;
        }

        public Employee build() {
            return new Employee(this);
        }
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

    public List<LocalDate> getSchedule() {return this.schedule;}

    public void setSchedule(List<LocalDate> schedule) { this.schedule = schedule;}

    public String getScheduleType() {return  this.scheduleType;}

    public void setScheduleType(String scheduleType) {this.scheduleType = scheduleType;}

    public int getBranchId() {return this.branchId;}

    public void setBranchId(int branchId) {this.branchId = branchId;}

    public void work() {
        if (schedule != null && schedule.contains(LocalDate.now())) {
            role.work(name, title, salary);
        } else {
            System.out.printf("%s doesn't work today%n", name);
        }
    }
}