package org.example;


import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
    void setRandomSalary() {
        Random random = new Random();
        this.salary = 120000 + random.nextInt(200000);
    }
    void setRandomTitle(){
        Random random = new Random();
        List<String> titles = Arrays.asList("джун", "мидл", "сеньор");
        this.title = titles.get(random.nextInt(titles.size()));
    }
}
