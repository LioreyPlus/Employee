package org.example.dto;

import org.example.Role;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public class EmployeeDto {
    private int id;
    private String name;
    private int salary;
    private String title;
    private Role role;
    private int branchId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;

    private String chiefName;
    private List<String> projects;

    public EmployeeDto() {}

    public EmployeeDto(int id, String name, int salary, String title, Role role, int branchId) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.title = title;
        this.role = role;
        this.branchId = branchId;
        this.hireDate = LocalDate.now(); // пример
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getSalary() { return salary; }
    public void setSalary(int salary) { this.salary = salary; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public int getBranchId() { return branchId; }
    public void setBranchId(int branchId) { this.branchId = branchId; }

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

    public String getChiefName() { return chiefName; }
    public void setChiefName(String chiefName) { this.chiefName = chiefName; }

    public List<String> getProjects() { return projects; }
    public void setProjects(List<String> projects) { this.projects = projects; }
}