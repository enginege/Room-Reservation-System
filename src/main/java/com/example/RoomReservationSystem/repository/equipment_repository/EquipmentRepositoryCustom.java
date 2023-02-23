package com.example.RoomReservationSystem.repository.equipment_repository;

import com.example.RoomReservationSystem.model.Equipment;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface EquipmentRepositoryCustom {
    List<String> findAllEquipmentNames();
    List<Equipment> findEquipmentsByEquipmentNames(Set<String> equipmentNames);
    List<Object[]> findEquipmentsInUse(Set<String> equipmentNames, Date start, Date end);
}
