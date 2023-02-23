package com.example.RoomReservationSystem.web.dto;

import java.util.Date;

public class ReservationJsonDto {
    private String title;
    private String userName;
    private String userSurName;
    private String roomName;
    private Date start;
    private Date end;

    private String description;

    public ReservationJsonDto() {
    }

    public ReservationJsonDto(String title, String userName, String userSurName, String roomName, Date start, Date end, String description) {
        this.title = title;
        this.userName = userName;
        this.userSurName = userSurName;
        this.roomName = roomName;
        this.start = start;
        this.end = end;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurName() {
        return userSurName;
    }

    public void setUserSurName(String userSurName) {
        this.userSurName = userSurName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
