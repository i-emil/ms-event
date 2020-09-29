package com.troojer.msevent.constraints.validator;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.constraints.ConsistentEventDuration;
import com.troojer.msevent.model.EventDateDto;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class EventDurationValidator
        implements ConstraintValidator<ConsistentEventDuration, EventDateDto> {

    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private int duration;

    @Override
    public void initialize(ConsistentEventDuration constraintAnnotation) {
        this.duration = constraintAnnotation.duration();
    }

    @Override
    public boolean isValid(EventDateDto eventDateDto, ConstraintValidatorContext context) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
        ZonedDateTime startDate;
        ZonedDateTime endDate;
        try {
            startDate = ZonedDateTime.parse(eventDateDto.getStart(), dateTimeFormatter);
            endDate = ZonedDateTime.parse(eventDateDto.getEnd(), dateTimeFormatter);
        } catch (Exception e) {
            return false;
        }

        long duration = Duration.between(startDate.toLocalDateTime(), endDate.toLocalDateTime()).toHours();
        logger.info("isValid(); duration: {}", duration);
        return duration <= this.duration;
    }
}
