package com.example.RoomReservationSystem.repository.category_repository;

import com.example.RoomReservationSystem.model.Category;
import com.example.RoomReservationSystem.model.Department;
import com.example.RoomReservationSystem.web.dto.CategoryDto;
import com.example.RoomReservationSystem.web.dto.DepartmentListDto;

import java.util.List;

public interface CategoryRepositoryCustom {
    Category findCategoryByName(String name);





    List<String> findAllCategoryNames();
}
