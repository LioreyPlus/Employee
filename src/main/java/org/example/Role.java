package org.example;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum Role {
    EMPLOYEE(Arrays.asList("specialist", "laboratory assistant", "senior specialist", "leading specialist", "expert")) {
        @Override
        public void work(String name, String title, int salary) {
            System.out.printf("Hi! I'm a worker, my name is %s, and I live on a salary of %d rubles%n", name, salary);
        }
    },
    MANAGER(Arrays.asList("efficiency manager", "supervisor", "group leader", "team leader")) {
        @Override
        public void work(String name, String title, int salary) {
            System.out.printf("Hi! I am in the position of %s. My name is %s and I get %d rubles%n", title.toLowerCase(), name, salary);
        }
    },
    DEVELOPER(Arrays.asList("junior", "middle", "senior")) {
        @Override
        public void work(String name, String title, int salary) {
            System.out.printf("Hi! My name is %s. I am in the position of %s and I earn %d rubles%n", name, title.toLowerCase(), salary);
        }
    },

    BRANCH_MANAGER(List.of("Branch Manager")) {
        @Override
        public void work(String name, String title, int salary) {
            System.out.println("Hi! I'm manager of this branch");
        }
    },
    DIRECTOR(List.of("CEO")) {
        @Override
        public void work(String name, String title, int salary) {
            System.out.printf("Sup! My name is %s, and I hold the position of %s. Everyone get to work!%n", name, title);
        }
    };

    private final List<String> availableTitles;
    private static final Random random = new Random();

    Role(List<String> availableTitles) {
        this.availableTitles = availableTitles;
    }

    public List<String> getAvailableTitles() {
        return availableTitles;
    }

    public String getRandomTitle() {
        return availableTitles.get(random.nextInt(availableTitles.size()));
    }

    public abstract void work(String name, String title, int salary);

    public int generateRandomSalary() {
        Random random = new Random();
        return switch (this) {
            case BRANCH_MANAGER -> 200000 + random.nextInt(200000); // 150k-250k
            case DEVELOPER -> 120000 + random.nextInt(200000);
            case MANAGER -> 80000 + random.nextInt(60000);
            case DIRECTOR -> 400000 + random.nextInt(200000);
            default -> 40000 + random.nextInt(50000);
        };
    }

    public Role getRequiredChiefRole() {
        return switch (this) {
            case EMPLOYEE, DEVELOPER -> Role.MANAGER;
            case MANAGER -> Role.BRANCH_MANAGER;
            case BRANCH_MANAGER -> Role.DIRECTOR;
            case DIRECTOR -> null;
        };
    }

    public String getScheduleType() {
        Random random = new Random();
        return switch (this) {
            case DIRECTOR, BRANCH_MANAGER, MANAGER, DEVELOPER -> "5/2";
            case EMPLOYEE -> random.nextBoolean() ? "5/2" : "2/2";
        };
    }
}