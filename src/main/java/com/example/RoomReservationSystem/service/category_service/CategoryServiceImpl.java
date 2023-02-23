package com.example.RoomReservationSystem.service.category_service;

import com.example.RoomReservationSystem.model.Category;
import com.example.RoomReservationSystem.repository.category_repository.CategoryRepository;
import com.example.RoomReservationSystem.web.dto.CategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Category findCategoryByName(String name) {
        return categoryRepository.findCategoryByName(name);
    }

    @Override
    public Category save(CategoryDto categoryDto) {
        Category category = new Category(
                categoryDto.getCategoryName()
        );
        return categoryRepository.save(category);
    }
    @Override
    public Category saveEdit(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Void deleteCategory(Category category) {
        category.setActive_status(false);
        saveEdit(category);
        return null;
    }

    @Override
    public List<String> findAllCategoryNames() {
        return categoryRepository.findAllCategoryNames();
    }
}
