package com.example.RoomReservationSystem.service.role_service;

import com.example.RoomReservationSystem.model.Role;
import com.example.RoomReservationSystem.model.User;
import com.example.RoomReservationSystem.repository.role_repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Set<Role> getUserRoles(User user) {
        return user.getRoles();
    }

    @Override
    public List<Role> getUserNotRoles(User user) {
        return roleRepository.getUserNotRoles(user.getId());
    }

    @Override
    public Role findByID(Long id) {
        return  roleRepository.findById(id).get();
    }
}
