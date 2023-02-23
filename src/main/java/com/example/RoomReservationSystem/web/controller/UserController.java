package com.example.RoomReservationSystem.web.controller;

import ch.qos.logback.core.util.ContextUtil;
import com.example.RoomReservationSystem.model.Role;
import com.example.RoomReservationSystem.model.User;
import com.example.RoomReservationSystem.service.department_service.DepartmentService;
import com.example.RoomReservationSystem.service.role_service.RoleService;
import com.example.RoomReservationSystem.service.user_service.UserService;
import com.example.RoomReservationSystem.web.dto.ReservationDto;
import com.example.RoomReservationSystem.web.dto.UserDto;
//import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    DepartmentService departmentService;

    //DISPLAY ALL USERS
    @PreAuthorize("hasAnyAuthority('CREATE', 'UPDATE', 'DELETE', 'MANAGE')")
    @GetMapping("/list")
    public String displayAllUsers(Authentication authentication, Model model){
        String username = authentication.getName();
        model.addAttribute("users", userService.findAllUsersOrderedByNameInCompany(username));
        return "users";
    }

    //EDIT USERNAME
    @PreAuthorize("hasAnyAuthority('CREATE', 'UPDATE', 'DELETE')")
    @GetMapping("/edit/{username}")
    public String editUser(HttpServletRequest req, @PathVariable("username") String userName, Model model){
        User user = userService.findByUserName(userName);
        if(user == null){
            return "redirect:/user/list";
        }
        if(user.getActive_status() == false){ //checks if the user is deleted
            return "redirect:/user/list";
        }

        User userTmp = (User) req.getSession().getAttribute("user");
        req.getSession().removeAttribute("user"); //Don't want to keep useless objects in session
        model.addAttribute("user", user);
        if (userTmp != null){
            model.addAttribute("user", userTmp);
        }

        model.addAttribute("userRoles", roleService.getUserRoles(user)); //Further Improvement: Get the roles by their importance order
        model.addAttribute("userNotRoles", roleService.getUserNotRoles(user)); //Further Improvement: Get the roles by their importance order
        model.addAttribute("departmentNames", departmentService.findAllDepartments());
        return "user_edit";
    }

    @PreAuthorize("hasAnyAuthority('CREATE', 'DELETE')")
    @PostMapping("/save/{user_id}")
    public String saveUser(@PathVariable("user_id") Long user_id,
                           @ModelAttribute("user") User user,
                           HttpSession session,
                           BindingResult result,
                           RedirectAttributes redirectAttributes){
        //model.addAttribute("user", user);
        //TODO: ADD ERROR MESSAGES FOR WRONG OR EMPTY INPUTS.
        int limit = 25;

        User updatedUser = userService.findByID(user_id);
//        User foo = userService.findByUserName(user.getUsername());


        if(result.hasErrors()){
            session.setAttribute("user", user);
            redirectAttributes.addFlashAttribute("error", "An error occurred while editing the user!");
            return "redirect:/user/edit/" + user.getUsername();
        }


//        String isExist = userService.findOtherByUserNameOrEmail(user.getUsername(), user.getEmail(), user.getId());
//        if (isExist.equals("bothExists")) {
//            session.setAttribute("user", user);
//            redirectAttributes.addFlashAttribute("error_UsedBoth", "Both Username and E-mail are already taken!");
//            return "redirect:/user/edit/" + updatedUser.getUsername();
//        }
//        else if(isExist.equals("usernameExists")) {
//            session.setAttribute("user", user);
//            redirectAttributes.addFlashAttribute("error_UsedUserName", "Username is already taken!");
//            return "redirect:/user/edit/" + updatedUser.getUsername();
//        }
//        else if(isExist.equals("emailExists")) {
//            session.setAttribute("user", user);
//            redirectAttributes.addFlashAttribute("error_UsedEmail", "E-mail is already taken!");
//            return "redirect:/user/edit/" + updatedUser.getUsername();
//        }


        if(user.getUsername()=="" || user.getUsername().length() > limit){
            session.setAttribute("user", user);
            redirectAttributes.addFlashAttribute("error_Username", "User's username must not be empty or more than " + limit + " characters!");
            return "redirect:/user/edit/" + updatedUser.getUsername();
        }
        if(user.getName()=="" || user.getName().length() > limit){
            session.setAttribute("user", user);
            redirectAttributes.addFlashAttribute("error_Name", "User's name must not be empty or more than " + limit + " characters!");
            return "redirect:/user/edit/" + updatedUser.getUsername();
        }
        if(user.getSurname()=="" || user.getSurname().length() > limit){
            session.setAttribute("user", user);
            redirectAttributes.addFlashAttribute("error_Surname", "User's surname must not be empty or more than " + limit + " characters!");
            return "redirect:/user/edit/" + updatedUser.getUsername();
        }
        //TODO: add further checks for email such as: includes "@" and "." etc.
        if(user.getEmail()=="" || user.getEmail().length() > 2*limit){
            session.setAttribute("user", user);
            redirectAttributes.addFlashAttribute("error_Email", "User's e-mail must not be empty or more than " + limit + " characters!");
            return "redirect:/user/edit/" + updatedUser.getUsername();
        }
        if(user.getDepartment()==null){
            session.setAttribute("user", user);
            redirectAttributes.addFlashAttribute("error_Department", "User's department must not be empty!");
            return "redirect:/user/edit/" + updatedUser.getUsername();
        }
        Boolean ifChanged = true;
        if (!updatedUser.getName().equals(user.getName())){
            updatedUser.setName(user.getName());
            ifChanged = false;
        }
        if (!updatedUser.getSurname().equals(user.getSurname())){
            updatedUser.setSurname(user.getSurname());
            ifChanged = false;
        }
//        if (!updatedUser.getUsername().equals(user.getUsername())){
//            updatedUser.setUsername(user.getUsername());
//            ifChanged = false;
//        }
        if (!updatedUser.getEmail().equals(user.getEmail())){
            updatedUser.setEmail(user.getEmail());
            ifChanged = false;
        }
        if (!updatedUser.getDepartment().equals(user.getDepartment())){
            updatedUser.setDepartment(user.getDepartment());
            ifChanged = false;
        }

        userService.saveEdit(updatedUser);
        redirectAttributes.addFlashAttribute("success", "User is successfully edited!");
        return "redirect:/user/list";
    }

    @PreAuthorize("hasAnyAuthority('CREATE', 'DELETE')")
    @PostMapping("/assign/{role_id}/{user_id}")
    public String assignRole(@PathVariable("role_id") Long role_id, @PathVariable("user_id") Long user_id, Model model) {
        //model.addAttribute("role_id", role_id);
        //model.addAttribute("user_id", user_id);
        User user = userService.findByID(user_id);
        Role role = roleService.findByID(role_id);
        if (user != null)
        {
            user.addRole(role);
            userService.saveEdit(user);
        }
        return "redirect:/user/edit/" + user.getUsername();
    }

    @PreAuthorize("hasAnyAuthority('CREATE', 'DELETE')")
    @PostMapping("/unassign/{role_id}/{user_id}")
    public String unassignRole(@PathVariable("role_id") Long role_id, @PathVariable("user_id") Long user_id, Model model) {
        //model.addAttribute("role_id", role_id);
        //model.addAttribute("user_id", user_id);
        User user = userService.findByID(user_id);
        Role role = roleService.findByID(role_id);
        user.removeRole(role);
        userService.saveEdit(user);
        return "redirect:/user/edit/" + user.getUsername();
    }

    //ADD USER
    @PreAuthorize("hasAuthority('CREATE')")
    @GetMapping("/new")
    public String newUser(HttpServletRequest req, Model model){

        UserDto userDtoTmp = (UserDto) req.getSession().getAttribute("UserDto");
        req.getSession().removeAttribute("UserDto"); //Important, you don't want to keep useless objects in your session
        model.addAttribute("UserDto", new UserDto());
        if (userDtoTmp != null){
            model.addAttribute("UserDto", userDtoTmp);
        }

        model.addAttribute("DepartmentNames", departmentService.findAllDepartmentNames());
        return "user_add";
    }

    @PreAuthorize("hasAuthority('CREATE')")
    @PostMapping("/new/add")
    public String addUser(@ModelAttribute("UserDto") UserDto userDto,
                          HttpSession session,
                          BindingResult result,
                          Model model,
                          RedirectAttributes redirectAttributes){

        if(result.hasErrors()){
            session.setAttribute("UserDto", userDto);
            redirectAttributes.addFlashAttribute("error", "An error occurred while adding a new user!");
            return "redirect:/user/new";
        }

        int limit = 25;
        if(userDto.getUserName()=="" || userDto.getUserName().length() > limit){
            session.setAttribute("UserDto", userDto);
            redirectAttributes.addFlashAttribute("error_Username", "User's username must not be empty or more than " + limit + " characters!");
            return "redirect:/user/new";
        }
        if(userDto.getFirstName()=="" || userDto.getFirstName().length() > limit){
            session.setAttribute("UserDto", userDto);
            redirectAttributes.addFlashAttribute("error_Name", "User's name must not be empty or more than " + limit + " characters!");
            return "redirect:/user/new";
        }
        if(userDto.getLastName()=="" || userDto.getLastName().length() > limit){
            session.setAttribute("UserDto", userDto);
            redirectAttributes.addFlashAttribute("error_Surname", "User's surname must not be empty or more than " + limit + " characters!");
            return "redirect:/user/new";
        }
        //TODO: add further checks for email such as: includes "@" and "." etc.
        if(userDto.getEmail()=="" || userDto.getEmail().length() > 2*limit){
            session.setAttribute("UserDto", userDto);
            redirectAttributes.addFlashAttribute("error_Email", "User's e-mail must not be empty or more than " + limit + " characters!");
            return "redirect:/user/new";
        }
        if(userDto.getDepartmentName()==""){
            session.setAttribute("UserDto", userDto);
            redirectAttributes.addFlashAttribute("error_Department", "User's department must not be empty!");
            return "redirect:/user/new";
        }

        String isExist = userService.findByUserNameOrEmail(userDto.getUserName(), userDto.getEmail());
        if (isExist.equals("bothExists")) {
            session.setAttribute("UserDto", userDto);
            redirectAttributes.addFlashAttribute("error_UsedBoth", "Both Username and E-mail are already taken!");
            return "redirect:/user/new";
        }
        else if(isExist.equals("usernameExists")) {
            session.setAttribute("UserDto", userDto);
            redirectAttributes.addFlashAttribute("error_UsedUserName", "Username is already taken!");
            return "redirect:/user/new";
        }
        else if(isExist.equals("emailExists")) {
            session.setAttribute("UserDto", userDto);
            redirectAttributes.addFlashAttribute("error_UsedEmail", "E-mail is already taken!");
            return "redirect:/user/new";
        }
        else
        userService.save(userDto); //Maybe assign the role at the bottom of the hierarchy while creating a new user.
        redirectAttributes.addFlashAttribute("success", "You have successfully added a new user!");
        return "redirect:/user/list";
    }

    @PreAuthorize("hasAuthority('DELETE')")
    @PostMapping("/delete/{username}")
    public String deleteUser(@PathVariable("username") String userName, Model model){
        User user = userService.findByUserName(userName);
        if (user == null) {
            return "redirect:/user/list";
        }
        userService.deleteUser(user);
        return "redirect:/user/list";
    }
}
