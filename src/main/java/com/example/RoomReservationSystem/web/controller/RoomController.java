package com.example.RoomReservationSystem.web.controller;

import com.example.RoomReservationSystem.model.Room;
import com.example.RoomReservationSystem.service.room_service.RoomService;
import com.example.RoomReservationSystem.web.dto.RoomDto;
import com.example.RoomReservationSystem.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/room")
public class RoomController {

    @Autowired
    RoomService roomService;

    @PreAuthorize("hasAnyAuthority('CREATE', 'UPDATE', 'DELETE')")
    @GetMapping("/list")
    public String  displayAllUsers(Model model, Authentication authentication){
        String username = authentication.getName();
        model.addAttribute("rooms", roomService.findAllRoomsInCompany(username));
        return "rooms";
    }

    @PreAuthorize("hasAuthority('DELETE')")
    @PostMapping("/delete/{id}")
    public String deleteRoom(@PathVariable("id") Long room_id, Model model){
        Room room = roomService.findByRoomId(room_id);
        if (room == null) {
            return "redirect:/room/list";
        }
        roomService.deleteRoom(room);
        return "redirect:/room/list";
    }
    @PreAuthorize("hasAuthority('CREATE')")
    @GetMapping("/new")
    public String newRoom (Model model ) {
        model.addAttribute("RoomDto", new RoomDto());
        return "room_add";
    }

    @PreAuthorize("hasAuthority('CREATE')")
    @PostMapping("/new/add")
    public String addRoom(@ModelAttribute("RoomDto") RoomDto roomDto, BindingResult result, Authentication authentication) {
        if (result.hasErrors()) {
            return "redirect:/room/new";
        }

        String username = authentication.getName();
        String companyName = roomService.getCompanyNameByUsername(username);
        roomService.save(roomDto, companyName);
        return "redirect:/room/list";
    }

    @PreAuthorize("hasAnyAuthority('CREATE', 'UPDATE', 'DELETE')")
    @GetMapping("/edit/{id}")
    public String editRoom(@PathVariable("id") Long room_id, Model model){
        Room room = roomService.findByRoomId(room_id);
        if(room == null){
            return "redirect:/room/list";
        }
        if(room.getActive_status() == false){
            return "redirect:/room/list";
        }
        model.addAttribute("room", room);
        return "room_edit";
    }

    @PreAuthorize("hasAnyAuthority('CREATE', 'UPDATE', 'DELETE')")
    @PostMapping("/save/{room_id}")
    public String saveRoom(@PathVariable("room_id") Long room_id, @ModelAttribute("room") Room room, BindingResult result, Model model){


        if(result.hasErrors()){
            return "redirect:/room/edit/" + room.getId() ;
        }
        Room updatedRoom = roomService.findByRoomId(room_id);
        if(room.getName()==null){
            return "redirect:/room/edit/" + room.getId();
        }
        if(room.getCapacity()==null){
            return "redirect:/room/edit/" + room.getId();
        }
        updatedRoom.setName(room.getName());
        updatedRoom.setCapacity(room.getCapacity());
        roomService.saveEdit(updatedRoom);
        return "redirect:/room/list";
    }
}
