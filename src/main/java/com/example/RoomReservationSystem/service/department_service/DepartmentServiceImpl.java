package com.example.RoomReservationSystem.service.department_service;

import com.example.RoomReservationSystem.model.Department;
import com.example.RoomReservationSystem.repository.company_repository.CompanyRepository;
import com.example.RoomReservationSystem.repository.department_repository.DepartmentRepository;
import com.example.RoomReservationSystem.web.dto.DepartmentListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService{

    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    CompanyRepository companyRepository;

    //public Department save(DepartmentRegistrationDto)

    @Override
    public List<DepartmentListDto> findAllDepartmentsOrdered(){
        return departmentRepository.findAllDepartmentsOrdered();
    }

    @Override
    public String companyNameByDepartmentName(String departmentName){
        return departmentRepository.companyNameByDepartmentName(departmentName);
    }

    @Override
    public List<Department> findAllDepartments(){
        return departmentRepository.findAllDepartments();
    }

    @Override
    public List<String> findAllDepartmentNames() {
        return departmentRepository.findAllDepartmentNames();
    }

    @Override
    public Department save(DepartmentListDto departmentListDto) {
        Department department = new Department(
                departmentListDto.getDepartmentName(),
                companyRepository.getCompanyByCompanyName(departmentListDto.getCompanyName())
                );
        return departmentRepository.save(department);
    }

    @Override
    public Department findByDepartmentName(String departmentName) {
        return departmentRepository.findByDepartmentName(departmentName);
    }

    @Override
    public Department saveEdit(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public Void deleteDepartment(Department department) {
        department.setActive_status(false);
        saveEdit(department);
        return null;
    }

}
