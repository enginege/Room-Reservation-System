package com.example.RoomReservationSystem.repository.reservation_repository;

import com.example.RoomReservationSystem.enums.Reservation_Status;
import com.example.RoomReservationSystem.model.Reservation;
import com.example.RoomReservationSystem.web.dto.ReservationDto;
import com.example.RoomReservationSystem.web.dto.ReservationListDto;
import com.example.RoomReservationSystem.web.dto.ReservationJsonDto;

import java.util.Date;
import java.util.List;

public interface ReservationRepositoryCustom {
    List<ReservationJsonDto> findAllReservationsInCompany(String companyName);
    List<ReservationJsonDto> findAllReservationsInCompanyInRoom(String companyName, Long room_id);
    List<ReservationListDto> findReservationsInCompanyWithStatus(String companyName, Reservation_Status reservation_status);
    List<ReservationListDto> findReservationsInCompanyWithoutStatus(String companyName);
    List<ReservationListDto> findReservationsInCompanyWithoutStatusWithUsername(String companyName, String userName);
    List<Reservation> findConflictingApprovedReservationsInRoom(Long room_id, Date start, Date end);
    List<Reservation> findConflictingPendingReservationsInRoom(Long room_id, Date start, Date end);
    List<Reservation> findOtherConflictingReservationsInRoom(Long reservation_id, Long room_id, Date start, Date end);
    List<ReservationListDto> findConflictingReservationsInRoom(Long room_id, Date start, Date end);
    List<String> findEmailsOfParticipants(Long reservation_id, String creatorName);
}
