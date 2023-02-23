package com.example.RoomReservationSystem.repository.company_repository;

import com.example.RoomReservationSystem.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long>, CompanyRepositoryCustom {

}
