package org.example;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Project {
    private int id;
    private String name;

    public Project() {
    }

    public Project(int id, String name) {
        this.id = id;
        this.name = name;
    }

}