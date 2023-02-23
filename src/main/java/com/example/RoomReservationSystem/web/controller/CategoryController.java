package com.example.RoomReservationSystem.web.controller;

import com.example.RoomReservationSystem.model.Category;
import com.example.RoomReservationSystem.model.Department;
import com.example.RoomReservationSystem.service.category_service.CategoryService;
import com.example.RoomReservationSystem.web.dto.CategoryDto;
import com.example.RoomReservationSystem.web.dto.DepartmentListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PreAuthorize("hasAnyAuthority('CREATE', 'UPDATE', 'DELETE', 'MANAGE')")
    @GetMapping("/list")
    public String displayCategoryNames(Model model){
        model.addAttribute("categories", categoryService.findAllCategoryNames());
        return "categories";

    }

    @PreAuthorize("hasAuthority('CREATE')")
    @GetMapping("/new")
    public String newCategory(Model model){
        model.addAttribute("CategoryDto",new CategoryDto());
        return "category_add";
    }

    @PreAuthorize("hasAuthority('CREATE')")
    @PostMapping("/new/add")
    public String addCategory(@ModelAttribute("CategoryDto") CategoryDto categoryDto, BindingResult result){
//        model.addAttribute("CategoryDto", categoryDto);
        if(result.hasErrors()){
            return "redirect:/category/new";
        }
        if(categoryService.findCategoryByName(categoryDto.getCategoryName()) != null) {
            return "redirect:/category/new";
        }
        categoryService.save(categoryDto);
        return "redirect:/category/list";
    }

    @PreAuthorize("hasAuthority('DELETE')")
    @PostMapping("/delete/{categoryName}")
    public String deleteCategory(@PathVariable("categoryName") String categoryName){
        Category category = categoryService.findCategoryByName(categoryName);
        if (category == null) {
            return "redirect:/user/list";
        }
        categoryService.deleteCategory(category);

        return "redirect:/category/list";
    }
}
