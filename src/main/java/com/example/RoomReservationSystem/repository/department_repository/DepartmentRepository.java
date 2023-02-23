package com.example.RoomReservationSystem.repository.department_repository;

import com.example.RoomReservationSystem.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long>, DepartmentRepositoryCustom {
}
