package com.example.RoomReservationSystem.service.role_service;

import com.example.RoomReservationSystem.model.Role;
import com.example.RoomReservationSystem.model.User;

import java.util.List;
import java.util.Set;

public interface RoleService {
    List<Role> findAll();
    Set<Role> getUserRoles(User user);
    List<Role> getUserNotRoles(User user);
    Role findByID(Long id);
}
