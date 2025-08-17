package org.example;

public enum Role {
    EMPLOYEE {
        @Override
        public void work(String name, String title, int salary) {
            System.out.printf("Hi! I'm a worker, my name is %s, and I live on a salary of %d rubles%n", name, salary);
        }
    },
    MANAGER {
        @Override
        public void work(String name, String title, int salary) {
            System.out.printf("Hi! I am in the position of %s. My name is %s and I get %d rubles%n", title.toLowerCase(), name, salary);
        }
    },
    DEVELOPER {
        @Override
        public void work(String name, String title, int salary) {
            System.out.printf("Hi! My name is %s. I am in the position of %s and I earn %d rubles%n", name, title.toLowerCase(), salary);
        }
    },

    DIRECTOR {  // Новая роль
        @Override
        public void work(String name, String title, int salary) {
            System.out.printf("Sup! My name is %s, and I hold the position of %s. Everyone get to work!%n", name, title);
        }
    };

    public abstract void work(String name, String title, int salary);
}
