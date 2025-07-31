package org.example;

public class Manager extends Employee {
    protected String title;

    public Manager(String name, int salary, String title) {
        super(name, salary);
        this.title = title;
    }
    public Manager(String name, int salary) {
        this(name, salary, "менеджер");
    }


    @Override
    void work() {
        System.out.printf("Привет! Я менеджер на должности %s. Меня зовут %s и я получаю %d рублей%n", title.toLowerCase(), name, salary);
    }
}
