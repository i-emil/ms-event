package com.troojer.msevent.mapper;

import com.troojer.msevent.model.StartEndDatesDto;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DatesMapper {

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");

    public static String entityToDto(ZonedDateTime startDate) {
        return startDate.format(formatter);
    }

    public static ZonedDateTime dtoToEntity(String dateDto) {
        return ZonedDateTime.parse(dateDto, formatter);
    }
}
