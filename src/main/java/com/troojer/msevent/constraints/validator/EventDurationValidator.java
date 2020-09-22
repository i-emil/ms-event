package com.troojer.msevent.constraints.validator;

import com.troojer.msevent.constraints.ConsistentEventDuration;
import com.troojer.msevent.model.EventDateDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class EventDurationValidator
        implements ConstraintValidator<ConsistentEventDuration, EventDateDto> {

    @Override
    public boolean isValid(EventDateDto eventDateDto, ConstraintValidatorContext context) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
        ZonedDateTime startDate;
        ZonedDateTime endDate = null;
        try {
            startDate = ZonedDateTime.parse(eventDateDto.getStart(), dateTimeFormatter);
            endDate = ZonedDateTime.parse(eventDateDto.getEnd(), dateTimeFormatter);
        } catch (Exception e) {
            return false;
        }

        long duration = Duration.between(startDate.toLocalDateTime(), endDate.toLocalDateTime()).toHours();
        return duration <= 24;
    }
}
