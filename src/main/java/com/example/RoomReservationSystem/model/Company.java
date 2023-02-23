package com.example.RoomReservationSystem.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name= "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "active_status")
    private Boolean active_status = true;

    public Company() {
    }

    public Company(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive_status() {
        return active_status;
    }

    public void setActive_status(Boolean active_status) {
        this.active_status = active_status;
    }

    /*public Collection<Department> getDepartments() {
        return departments;
    }

    //Rather than set departments, using add and remove department methods to ensure the logic.
    public void addDepartments(Department department) {
        this.departments.add(department);
    }

    public void removeDepartments(Department department) {
        this.departments.remove(department);
    }*/
}
