package com.troojer.msevent.mapper;

import com.troojer.msevent.model.EventDateDto;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class EventDateMapper {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");

    public static EventDateDto entityDatesToDto(ZonedDateTime startDate, ZonedDateTime endDate) {
        return EventDateDto.builder()
                .start(startDate.format(formatter))
                .end(endDate.format(formatter))
                .build();
    }

    public static ZonedDateTime dtoToStartDate(EventDateDto dateDto) {
        return ZonedDateTime.parse(dateDto.getStart(), formatter);
    }

    public static ZonedDateTime dtoToEndDate(EventDateDto dateDto) {
        return ZonedDateTime.parse(dateDto.getEnd(), formatter);
    }
}
