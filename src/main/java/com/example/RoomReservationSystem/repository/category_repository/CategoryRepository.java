package com.example.RoomReservationSystem.repository.category_repository;

import com.example.RoomReservationSystem.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom{
}
