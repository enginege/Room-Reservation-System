package com.example.RoomReservationSystem.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

public enum Reservation_Status {
    PENDING("P"), APPROVED("A"), DECLINED("D"), FINISHED("F"), CONFLICTING("C"), REVOKED("R");

    private String code;
    private Reservation_Status(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}


