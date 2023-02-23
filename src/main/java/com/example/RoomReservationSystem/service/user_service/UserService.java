package com.example.RoomReservationSystem.service.user_service;

import com.example.RoomReservationSystem.model.User;
import com.example.RoomReservationSystem.web.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService extends UserDetailsService {
    User save(UserDto userDto);

    User saveEdit(User user);
    List<UserDto> findAllUsersOrderedByNameInCompany(String username);

    List<UserDto> findAllOtherUsersOrderedByNameInCompany(String username);

    User findByUserName(String userName);

    User findByID(Long id);
    Void deleteUser(User user);
    String findByUserNameOrEmail(String userName, String email);
    String findOtherByUserNameOrEmail(String userName, String email, Long user_id);
}
