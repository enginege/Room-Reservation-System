package com.example.RoomReservationSystem.service.equipment_service;

import com.example.RoomReservationSystem.model.Equipment;
import com.example.RoomReservationSystem.repository.equipment_repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class EquipmentServiceImpl implements EquipmentService {
    @Autowired
    EquipmentRepository equipmentRepository;

    @Override
    public List<String> findAllEquipmentNames() {
        return equipmentRepository.findAllEquipmentNames();
    }

//    @Override
//    public List<Equipment> findEquipmentsByEquipmentNames(Set<String> equipmentNames) {
//        return equipmentRepository.findEquipmentsByEquipmentNames(equipmentNames);
//    }
}
