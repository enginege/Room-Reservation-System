package com.example.RoomReservationSystem.repository.room_repository;

import com.example.RoomReservationSystem.model.Room;
import com.example.RoomReservationSystem.model.User;
import com.example.RoomReservationSystem.web.dto.RoomDto;
import com.example.RoomReservationSystem.web.dto.UserDto;

import java.util.List;

public interface RoomRepositoryCustom {
    List<RoomDto> findAllRoomsInCompany(String companyName);


    List<RoomDto> findAllRoomsOrderedByName();

    Room findByRoomName(String name);
}
