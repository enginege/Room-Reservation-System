package com.example.RoomReservationSystem.service.category_service;

import com.example.RoomReservationSystem.model.Category;
import com.example.RoomReservationSystem.web.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    Category findCategoryByName(String name);

    Category save(CategoryDto categoryDto);

    Category saveEdit(Category category);

    Void deleteCategory(Category category);

    List<String> findAllCategoryNames();
}
