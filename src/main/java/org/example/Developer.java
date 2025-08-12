package org.example;

import java.util.List;

public class Developer extends Employee {

    Developer(String name, int salary, List<DayOfWeek> weekend, String title) {
        this.name = name;
        this.salary = salary;
        this.weekend = weekend;
        this.title = title;

    }
    public Developer(String name, int salary, List<DayOfWeek> weekends) {
        this(name, salary, weekends, "менеджер");
    }

    public Developer(String name, int salary) {
        this(name, salary, List.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY), "менеджер");
    }
    public Developer(String name) {
        this(name, 0, List.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY), "менеджер");
    }


    @Override
    void work() {
        System.out.printf("Привет! Меня зовут %s. Я нахожусь на должности %s и я зарабатываю %d рублей%n", name, title.toLowerCase(), salary);
    }
    public String getType() {return "Developer";}
}
