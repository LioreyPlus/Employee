package org.example;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DayOfWeek dow = DayOfWeek.SUNDAY;
        Employee rabotyaga = new Employee("Александр", 70000, List.of(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY));
        if (rabotyaga.isWork(dow)){
            rabotyaga.work();
        }


        Manager rg = new Manager("Данил", 95000, "Руководитель группы");
        if (rg.isWork(dow)){
            rg.work();
        }

        Developer dev = new Developer("Михаил", 250000, "Старший разработчик");
        if (dev.isWork(dow)) {
            dev.work();
        }
    }
}




