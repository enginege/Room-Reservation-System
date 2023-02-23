package com.example.RoomReservationSystem.repository.room_repository;

import com.example.RoomReservationSystem.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomRepositoryCustom {
}
