package com.example.RoomReservationSystem.service.company_service;

import com.example.RoomReservationSystem.model.Company;
import com.example.RoomReservationSystem.repository.company_repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService{
    @Autowired
    CompanyRepository companyRepository;

    @Override
    public List<String> findAllCompanyNames() { return  companyRepository.findAllCompanyNames();}


}
