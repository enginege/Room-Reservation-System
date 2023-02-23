package com.example.RoomReservationSystem.service.department_service;

import com.example.RoomReservationSystem.model.Department;
import com.example.RoomReservationSystem.model.User;
import com.example.RoomReservationSystem.web.dto.DepartmentListDto;

import java.util.List;

public interface DepartmentService {


    List<DepartmentListDto> findAllDepartmentsOrdered();
    String companyNameByDepartmentName(String departmentName);
    List<Department> findAllDepartments();
    List<String> findAllDepartmentNames();
    Department save(DepartmentListDto departmentListDto);
    Department findByDepartmentName(String departmentName);
    Department saveEdit(Department department);
    Void deleteDepartment(Department department);
}
