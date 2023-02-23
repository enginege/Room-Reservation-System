package com.example.RoomReservationSystem.service.reservation_service;

import com.example.RoomReservationSystem.enums.Reservation_Status;
import com.example.RoomReservationSystem.model.Reservation;
import com.example.RoomReservationSystem.web.dto.ReservationDto;
import com.example.RoomReservationSystem.web.dto.ReservationListDto;
import com.example.RoomReservationSystem.web.dto.ReservationJsonDto;

import java.util.Date;
import java.util.List;

public interface ReservationService {
    List<ReservationListDto> findReservationsInCompanyWithoutStatus(String username);
    List<ReservationListDto> findReservationsInCompanyWithoutStatusWithUsername(String username);
    List<ReservationJsonDto> findAllReservationsInCompany(String username);
    List<ReservationJsonDto> findAllReservationsInCompanyInRoom(String username, Long room_id);
    List<Reservation> findAll();
    List<ReservationListDto> findReservationsInCompanyWithStatus(String username, Reservation_Status reservation_status);
    String save(Long room_id, String userName, ReservationDto reservationDto);
    String saveResubmit(Long room_id, Reservation reservation);
    Reservation saveApproved(Reservation reservation);
    Reservation saveEdit(Reservation reservation);
    Reservation saveFinished(Reservation reservation);
    Reservation findById(Long reservation_id);
    List<ReservationListDto> findConflictingReservationsInRoom(Long room_id, Date start, Date end);
    List<String> findEmailsOfParticipants(Long reservation_id, String creatorName);
}
