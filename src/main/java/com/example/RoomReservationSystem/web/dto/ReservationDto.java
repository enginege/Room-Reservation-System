package com.example.RoomReservationSystem.web.dto;

import com.example.RoomReservationSystem.enums.Reservation_Status;
import com.example.RoomReservationSystem.model.Equipment;
import com.example.RoomReservationSystem.model.Reservation;
import com.example.RoomReservationSystem.model.User;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ReservationDto {
    private Long reservation_id;
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date start;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date end;
    private String creator;
    private Boolean urgency;
    private Boolean cleaningService;
    private Boolean foodService;
    private String notes;
    private String status;
    private String roomName;
    private Set<String> equipmentNames;
    private Set<String> userNames;

    public ReservationDto() {
    }

    public ReservationDto(String name, Date start, Date end, String creator, Boolean urgency, Boolean cleaningService, Boolean foodService, String notes, String status, String roomName, Set<String> equipmentNames) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.creator = creator;
        this.urgency = urgency;
        this.cleaningService = cleaningService;
        this.foodService = foodService;
        this.notes = notes;
        this.status = status;
        this.roomName = roomName;
        this.equipmentNames = equipmentNames;
    }

    public Long getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(Long reservation_id) {
        this.reservation_id = reservation_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Boolean getUrgency() {
        return urgency;
    }

    public void setUrgency(Boolean urgency) {
        this.urgency = urgency;
    }

    public Boolean getCleaningService() {
        return cleaningService;
    }

    public void setCleaningService(Boolean cleaningService) {
        this.cleaningService = cleaningService;
    }

    public Boolean getFoodService() {
        return foodService;
    }

    public void setFoodService(Boolean foodService) {
        this.foodService = foodService;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Set<String> getEquipmentNames() {
        return equipmentNames;
    }

    public void setEquipmentNames(Set<String> equipmentNames) {
        this.equipmentNames = equipmentNames;
    }

    public void addEquipmentNames(String equipmentName) {
        this.equipmentNames.add(equipmentName);
    }

    public void removeEquipmentNames(String equipmentName) {
        this.equipmentNames.remove(equipmentName);
    }

    public Set<String> getUserNames() {
        return userNames;
    }

    public void setUserNames(Set<String> userNames) {
        this.userNames = userNames;
    }

    public void addUserName(String userName) {
        this.userNames.add(userName);
    }

    public void removeUserName(String userName) {
        this.userNames.remove(userName);
    }
}
