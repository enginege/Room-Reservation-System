package com.example.RoomReservationSystem.repository.department_repository;


import com.example.RoomReservationSystem.model.Department;
import com.example.RoomReservationSystem.web.dto.DepartmentListDto;

import java.util.List;

public interface DepartmentRepositoryCustom{


    List<DepartmentListDto> findAllDepartmentsOrdered();

    List<String> findAllDepartmentNames();

    Department findByDepartmentName(String departmentName);

    List<Department> findAllDepartments();
    String companyNameByDepartmentName(String departmentName);
}

