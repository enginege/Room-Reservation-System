package com.example.RoomReservationSystem.service.user_service;

import com.example.RoomReservationSystem.model.Authority;
import com.example.RoomReservationSystem.model.Role;
import com.example.RoomReservationSystem.model.User;
import com.example.RoomReservationSystem.repository.company_repository.CompanyRepository;
import com.example.RoomReservationSystem.repository.department_repository.DepartmentRepository;
import com.example.RoomReservationSystem.repository.user_repository.UserRepository;
import com.example.RoomReservationSystem.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.expression.Sets;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    CompanyRepository companyRepository;

    @Override
    public User save(UserDto userDto) {
        User user = new User(
                userDto.getFirstName()
                ,userDto.getLastName()
                ,userDto.getUserName()
                ,userDto.getEmail()
                ,bCryptPasswordEncoder.encode(userDto.getPassword())
                ,departmentRepository.findByDepartmentName(userDto.getDepartmentName())
                /*,Set.of(new Role("user", "user"))*/);
        return userRepository.save(user);
    }

    @Override
    public User saveEdit(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<UserDto> findAllUsersOrderedByNameInCompany(String username){
        String companyName = companyRepository.getCompanyNameByUsername(username);
        return userRepository.findAllUsersOrderedByNameInCompany(companyName);
    }

    @Override
    public List<UserDto> findAllOtherUsersOrderedByNameInCompany(String username){
        String companyName = companyRepository.getCompanyNameByUsername(username);
        return userRepository.findAllOtherUsersOrderedByNameInCompany(username, companyName);
    }

    @Override
    public User findByUserName(String userName){
        return userRepository.findByUserName(userName);
    }

    @Override
    public User findByID(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public Void deleteUser(User user) {
        user.setActive_status(false);
        saveEdit(user);
        return null;
    }

    @Override
    public String findByUserNameOrEmail(String userName, String email) {
        return userRepository.findByUserNameOrEmail(userName, email);
    }


    @Override
    public String findOtherByUserNameOrEmail(String userName, String email, Long user_id) {
        return userRepository.findOtherByUserNameOrEmail(userName, email, user_id);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        if(user == null) {
            throw new UsernameNotFoundException("Invalid username or password!");
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername()
                , user.getPassword()
                , mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        Set<Authority> authorities = new HashSet<>();
        for(Role role : roles){
            authorities.addAll(role.getAuthorities());
        }
        return authorities.stream().map(authority -> new SimpleGrantedAuthority(authority.getName())).collect(Collectors.toList());
//        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

}
