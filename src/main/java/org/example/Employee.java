package org.example;

import java.util.List;

public class Employee {
    protected String name;
    protected int salary;
    protected List<DayOfWeek> weekend;

    public Employee(String name) {
        this(name, 0, List.of(DayOfWeek.SUNDAY, DayOfWeek.SATURDAY));
    }

    Employee(String name, int salary) {
        this(name, salary, List.of(DayOfWeek.SUNDAY, DayOfWeek.SATURDAY));
    }

    Employee(String name, int salary, List<DayOfWeek> weekend) {
        this.name = name;
        this.salary = salary;
        this.weekend = weekend;
    }


    boolean isWork(DayOfWeek dow) {
        if (weekend.contains(dow)) {
            System.out.printf("Сегодня %s. %s не работает%n", dow.getTranslation().toLowerCase(), name);
            return false;
        } else {
            System.out.printf("Сегодня %s. %s работает%n", dow.getTranslation().toLowerCase(), name);
            return true;
        }

    }

    void work() {
        System.out.println("Привет! Я работяга, меня зовут " + name + " и я живу на мою зпешечку в размере " + salary + " рублей");
    }
}


