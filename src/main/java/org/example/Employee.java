package org.example;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class Employee {
    private int id;
    private String name;
    private int salary;

    @Builder.Default
    private String scheduleType = "5/2";

    private List<LocalDate> schedule;

    @Builder.Default
    private String title = "specialist";

    @Builder.Default
    private Role role = Role.EMPLOYEE;

    private Employee chief;
    private int branchId;

    public void work() {
        if (schedule != null && schedule.contains(LocalDate.now())) {
            role.work(name, title, salary);
        } else {
            System.out.printf("%s doesn't work today%n", name);
        }
    }
}