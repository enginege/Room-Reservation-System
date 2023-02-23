package com.example.RoomReservationSystem.repository.user_repository;

import com.example.RoomReservationSystem.model.User;
import com.example.RoomReservationSystem.web.dto.UserDto;

import java.util.List;
import java.util.Set;

public interface UserRepositoryCustom {
    List<UserDto> findAllUsersOrderedByNameInCompany(String companyName);
    List<UserDto> findAllOtherUsersOrderedByNameInCompany(String userName, String companyName);
    User findByUserName(String userName);
    String findByUserNameOrEmail(String userName, String email);
    String findOtherByUserNameOrEmail(String userName, String email, Long user_id);
    List<User> findUsersByUserNames(Set<String> userNames);
    String findAnyReservationOfficerEmail();
}
