package org.example;

public class Branch {
    private int id;
    private String name;
    private String address;
    private int manager_id;


    public Branch(){}


    public Branch(int id, String name, String address, int manager_id) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.manager_id = manager_id;
    }

    public int getId() {return this.id;}
    public void setId(int ID) { this.id = id;}
    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}
    public String getAddress() {return this.address;}
    public void setAddress(String address) {this.address = address;}
    public int getManager_id() {return manager_id;}
    public void setManager_id(int manager_id) {this.manager_id = manager_id;}

}
