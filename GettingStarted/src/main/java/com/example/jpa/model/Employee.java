package com.example.jpa.model;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
public class Employee {

    @Id
    private int id;
    private String name;
    private long salary;

    public Employee() {}

    public Employee(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public long getSalary() {

        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }
}
