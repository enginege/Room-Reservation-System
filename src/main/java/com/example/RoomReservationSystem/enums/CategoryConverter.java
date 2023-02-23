package com.example.RoomReservationSystem.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class CategoryConverter implements AttributeConverter<Reservation_Status, String> {

    @Override
    public String convertToDatabaseColumn(Reservation_Status reservation_status) {
        if (reservation_status == null) {
            return null;
        }
        return reservation_status.getCode();
    }

    @Override
    public Reservation_Status convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(Reservation_Status.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
