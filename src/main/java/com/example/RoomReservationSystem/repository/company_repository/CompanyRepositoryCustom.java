package com.example.RoomReservationSystem.repository.company_repository;

import com.example.RoomReservationSystem.model.Company;

import java.util.List;

public interface CompanyRepositoryCustom {
    List<String> findAllCompanyNames();
    Company getCompanyByCompanyName(String companyName);
    String getCompanyNameByUsername(String username);
}
