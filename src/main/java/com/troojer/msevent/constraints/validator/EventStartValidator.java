package com.troojer.msevent.constraints.validator;

import com.troojer.msevent.constraints.ConsistentEventStart;
import com.troojer.msevent.model.EventDateDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class EventStartValidator
        implements ConstraintValidator<ConsistentEventStart, EventDateDto> {

    @Override
    public boolean isValid(EventDateDto eventDateDto, ConstraintValidatorContext context) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
        ZonedDateTime startDate;
        try {
            startDate = ZonedDateTime.parse(eventDateDto.getStart(), dateTimeFormatter);
        } catch (Exception e) {
            return false;
        }

        int period = Period.between(LocalDate.now(), startDate.toLocalDate()).getDays();
        return period <= 7;
    }
}
