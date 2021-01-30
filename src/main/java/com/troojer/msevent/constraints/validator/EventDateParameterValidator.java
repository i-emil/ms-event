package com.troojer.msevent.constraints.validator;

import com.troojer.msevent.constraints.EventDateParameters;
import com.troojer.msevent.model.StartEndDatesDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class EventDateParameterValidator
        implements ConstraintValidator<EventDateParameters, StartEndDatesDto> {

    @Override
    public boolean isValid(StartEndDatesDto startEndDatesDto, ConstraintValidatorContext context) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
        ZonedDateTime startDate;
        ZonedDateTime endDate;
        try {
            startDate = ZonedDateTime.parse(startEndDatesDto.getStart(), dateTimeFormatter);
            endDate = ZonedDateTime.parse(startEndDatesDto.getEnd(), dateTimeFormatter);
        } catch (Exception e) {
            return false;
        }
        return startDate.isAfter(ZonedDateTime.now()) && startDate.isBefore(endDate);
    }
}
