package com.example.RoomReservationSystem.service.equipment_service;

import com.example.RoomReservationSystem.model.Equipment;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface EquipmentService {
    List<String> findAllEquipmentNames();
//    List<Equipment> findEquipmentsByEquipmentNames(Set<String> equipmentNames);
}
