package org.example;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Branch {
    private int id;
    private String name;
    private String address;
    private int managerId;


    public Branch(){}


    public Branch(int id, String name, String address, int managerId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.managerId = managerId;
    }
}