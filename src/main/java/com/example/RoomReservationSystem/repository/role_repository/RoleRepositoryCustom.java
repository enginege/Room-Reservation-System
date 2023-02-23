package com.example.RoomReservationSystem.repository.role_repository;

import com.example.RoomReservationSystem.model.Role;

import java.util.List;

public interface RoleRepositoryCustom {
    List<Role> getUserNotRoles(Long id);
}
