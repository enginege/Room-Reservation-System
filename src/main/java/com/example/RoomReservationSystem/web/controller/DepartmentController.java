package com.example.RoomReservationSystem.web.controller;

import com.example.RoomReservationSystem.model.Company;
import com.example.RoomReservationSystem.model.Department;
import com.example.RoomReservationSystem.service.company_service.CompanyService;
import com.example.RoomReservationSystem.service.department_service.DepartmentService;
import com.example.RoomReservationSystem.web.dto.DepartmentListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    DepartmentService departmentService;
    @Autowired
    CompanyService companyService;


    @PreAuthorize("hasAnyAuthority('CREATE', 'UPDATE', 'DELETE')")
    @GetMapping("/list")
    public String displayAllDepartments(Model model){
        model.addAttribute("departments", departmentService.findAllDepartmentsOrdered());
        return "departments";
    }

    @PreAuthorize("hasAuthority('CREATE')")
    @GetMapping("/new")
    public String newDepartment(Model model){
        model.addAttribute("DepartmentDto",new DepartmentListDto());
        model.addAttribute("CompanyNames", companyService.findAllCompanyNames());
        return "department_add";
    }

    @PreAuthorize("hasAuthority('CREATE')")
    @PostMapping("/new/add")
    public String addCompany(@ModelAttribute("DepartmentDto") DepartmentListDto departmentDto, BindingResult result, Model model){
        model.addAttribute("DepartmentDto", departmentDto);
        if(result.hasErrors()){
            return "redirect:/department/new";
        }
        if(departmentService.findByDepartmentName(departmentDto.getDepartmentName()) != null) {
            return "redirect:/department/new";
        }
        departmentService.save(departmentDto);
        return "redirect:/department/list";
    }

    @PreAuthorize("hasAuthority( 'DELETE')")
    @PostMapping("/delete/{departmentName}")
    public String deleteDepartment(@PathVariable("departmentName") String departmentName, Model model){
        Department department = departmentService.findByDepartmentName(departmentName);
        if (department == null) {
            return "redirect:/user/list";
        }
        departmentService.deleteDepartment(department);

        return "redirect:/department/list";
    }


}
