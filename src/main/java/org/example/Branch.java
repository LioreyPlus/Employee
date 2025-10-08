package org.example;

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

    public int getId() {return this.id;}
    public void setId(int id) { this.id = id;}
    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}
    public String getAddress() {return this.address;}
    public void setAddress(String address) {this.address = address;}
    public int getManagerId() {return managerId;}
    public void setManagerId(int managerId) {this.managerId = managerId;}

}