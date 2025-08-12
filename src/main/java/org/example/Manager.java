package org.example;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Manager extends Employee {

    public Manager(String name, int salary, List<DayOfWeek> weekends, String title) {
        this.name = name;
        this.salary = salary;
        this.weekend = weekends;
        this.title = title;
    }
    public Manager(String name, int salary, List<DayOfWeek> weekends) {
        this(name, salary, weekends, "менеджер");
    }

    public Manager(String name, int salary) {
        this(name, salary, List.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY), "менеджер");
    }
    public Manager(String name) {
        this(name, 0, List.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY), "менеджер");
    }
    @Override
    void work() {
        System.out.printf("Привет! Я нахожусь на должности %s. Меня зовут %s и я получаю %d рублей%n", title.toLowerCase(), name, salary);
    }
    public String getType() {return "Manager";}
}
