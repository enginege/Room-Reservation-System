package com.example.RoomReservationSystem.web.dto;

import java.util.Set;

public class UserDto {
    private String firstName;

    //@NotEmpty(message = "Last name can't be empty!")
    private String lastName;

    //@NotEmpty(message = "Email name can't be empty!")
    //@Email(message = "*Please provide a valid Email")
    private String userName;

    //@Length(min = 5, message = "*Your password must have at least 5 characters")
    //@NotEmpty(message = "*Please provide your password")
    private String password;

    private String email;

    private String departmentName;

    private Set<String> roles;

    public UserDto() {
    }

    public UserDto(String firstName, String lastName, String userName, String password, String email, String departmentName, Set<String> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.departmentName = departmentName;
        this.roles = roles;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
