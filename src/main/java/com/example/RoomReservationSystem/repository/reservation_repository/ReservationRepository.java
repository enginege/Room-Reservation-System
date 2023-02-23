package com.example.RoomReservationSystem.repository.reservation_repository;

import com.example.RoomReservationSystem.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationRepositoryCustom {
}
