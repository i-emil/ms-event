package com.troojer.msevent.mapper;

import com.troojer.msevent.model.StartEndDatesDto;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class StartEndDatesMapper {

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");

    public static StartEndDatesDto entityDatesToDto(ZonedDateTime startDate, ZonedDateTime endDate) {
        return StartEndDatesDto.builder()
                .start(startDate.format(formatter))
                .end(endDate.format(formatter))
                .build();
    }

    public static ZonedDateTime dtoToStartDate(StartEndDatesDto dateDto) {
        return ZonedDateTime.parse(dateDto.getStart(), formatter);
    }

    public static ZonedDateTime dtoToEndDate(StartEndDatesDto dateDto) {
        return ZonedDateTime.parse(dateDto.getEnd(), formatter);
    }
}
