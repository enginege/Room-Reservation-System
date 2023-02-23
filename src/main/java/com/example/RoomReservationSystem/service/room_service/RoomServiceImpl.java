package com.example.RoomReservationSystem.service.room_service;

import com.example.RoomReservationSystem.model.Room;
import com.example.RoomReservationSystem.model.User;
import com.example.RoomReservationSystem.repository.company_repository.CompanyRepository;
import com.example.RoomReservationSystem.repository.room_repository.RoomRepository;
import com.example.RoomReservationSystem.web.dto.RoomDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    RoomRepository roomRepository;

    @Autowired
    CompanyRepository companyRepository;
    @Override
    public List<RoomDto> findAllRoomsOrderedByName(){
        return roomRepository.findAllRoomsOrderedByName();
    }
    @Override
    public Room findByRoomId(Long id){
        return roomRepository.findById(id).get();
    }

    @Override
    public Room saveEdit(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public List<RoomDto> findAllRoomsInCompany(String username) {
        String companyName = companyRepository.getCompanyNameByUsername(username);
        return roomRepository.findAllRoomsInCompany(companyName);
    }

    @Override
    public Void deleteRoom(Room room) {
        room.setActive_status(false);
        saveEdit(room);
        return null;
    }

    @Override
    public String getCompanyNameByUsername(String username) {
        String companyName = companyRepository.getCompanyNameByUsername(username);
        return companyName;
    }

    @Override
    public Room save(RoomDto roomDto, String companyName) {
        Room room = new Room(
                roomDto.getRoomName()
                ,roomDto.getCapacity()
                ,companyRepository.getCompanyByCompanyName(companyName));
        return roomRepository.save(room);
    }

}
