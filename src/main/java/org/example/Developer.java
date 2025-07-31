package org.example;

public class Developer extends Manager {

    public Developer(String name, int salary) {
        this(name, salary,"разработчик");
    }
    public Developer(String name, int salary, String title) {
        super(name, salary, title);
    }



    @Override
    void work() {
        System.out.printf("Привет! Меня зовут %s. Я нахожусь на должности %s и я зарабатываю %d рублей%n", name, title.toLowerCase(), salary);
    }
}
