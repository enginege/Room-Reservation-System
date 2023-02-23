package com.example.RoomReservationSystem.web.dto;

public class RoomDto {
    private Long roomId;
    private String roomName;
    private Integer capacity;
    private Boolean availability;
    private String companyName;

    public RoomDto() {
    }

    public RoomDto(String roomName, Integer capacity) {
        this.roomName = roomName;
        this.capacity = capacity;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
