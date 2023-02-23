package com.example.RoomReservationSystem.repository.equipment_repository;

import com.example.RoomReservationSystem.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Long>, EquipmentRepositoryCustom {
}
