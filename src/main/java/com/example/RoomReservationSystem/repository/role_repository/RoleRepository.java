package com.example.RoomReservationSystem.repository.role_repository;

import com.example.RoomReservationSystem.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long>, RoleRepositoryCustom {
}
