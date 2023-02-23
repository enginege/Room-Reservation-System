package com.example.RoomReservationSystem.service.room_service;

import com.example.RoomReservationSystem.model.Room;
import com.example.RoomReservationSystem.model.User;
import com.example.RoomReservationSystem.web.dto.RoomDto;
import com.example.RoomReservationSystem.web.dto.UserDto;

import java.util.List;

public interface RoomService {

    List<RoomDto> findAllRoomsOrderedByName();

    public Room findByRoomId(Long id);

    public Room saveEdit(Room room);

    List<RoomDto> findAllRoomsInCompany(String username);

    Void deleteRoom(Room room);

    String getCompanyNameByUsername(String username);

    Room save(RoomDto roomDto, String companyName);

}
