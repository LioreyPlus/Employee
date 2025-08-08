package org.example;

import java.util.*;

public class Employee {

    protected String name;
    protected int salary;
    protected List<DayOfWeek> weekend;
    protected String title;

    Employee(String name, int salary, List<DayOfWeek> weekend, String title) {
        this.name = name;
        this.salary = salary;
        this.weekend = weekend;
        this.title = title;
    }
    Employee(String name, int salary, List<DayOfWeek> weekend) {
        this (name, salary, weekend, "cпециалист");
    }
    Employee(String name, int salary) {
        this(name, salary, List.of(DayOfWeek.SUNDAY, DayOfWeek.SATURDAY), "cпециалист");
    }
    Employee(String name) {
        this(name, 0, List.of(DayOfWeek.SUNDAY, DayOfWeek.SATURDAY), "специалист");
    }
    Employee(){
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getSalary(){
        return this.salary;
    }

    public void setSalary(int salary){
        this.salary = salary;
    }

    public List<DayOfWeek> getWeekend(){
        return this.weekend;
    }

    public void setWeekend(List<DayOfWeek> weekend){
        this.weekend = weekend;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }



    void setRandomSalary(){
        Random random = new Random();
        this.salary = 40000 + random.nextInt(50000);
    }
    void setRandomWeekend(){
        Random random = new Random();
        List<DayOfWeek> allDays = Arrays.asList(DayOfWeek.values());
        Collections.shuffle(allDays);
        this.weekend =  allDays.subList(0, random.nextInt(3) + 1);
    }

    void setRandomTitle(){
        Random random = new Random();
        List<String> titles = Arrays.asList("специалист", "лаборант", "старший специалист", "ведущий специалист", "эксперт");
        this.title = titles.get(random.nextInt(titles.size()));
    }

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
        System.out.println("Привет! Я работяга, меня зовут " + name + " и я живу на мою зпешечку в размере " + salary + " рублей");
    }
}


