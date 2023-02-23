package com.example.RoomReservationSystem.web.dto;

public class DepartmentListDto {

    private String departmentName;

    private String companyName;


    public DepartmentListDto() {
    }

    public DepartmentListDto(String departmentName, String companyName) {
        this.departmentName = departmentName;
        this.companyName = companyName;

    }




    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
