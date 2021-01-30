package com.troojer.msevent.constraints.validator;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.constraints.ConsistentEventDuration;
import com.troojer.msevent.model.StartEndDatesDto;
import com.troojer.msevent.client.UserPlanConstantsClient;
import com.troojer.msevent.util.AccessCheckerUtil;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class EventDurationValidator
        implements ConstraintValidator<ConsistentEventDuration, StartEndDatesDto> {

    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private final AccessCheckerUtil accessChecker;

    private int duration;

    public EventDurationValidator(AccessCheckerUtil accessChecker) {
        this.accessChecker = accessChecker;
    }

    @Override
    public void initialize(ConsistentEventDuration constraintAnnotation) {
        this.duration = UserPlanConstantsClient.getMaxEventDurationHour(accessChecker.getPlan());
    }

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

        long duration = Duration.between(startDate.toLocalDateTime(), endDate.toLocalDateTime()).toHours();
        logger.info("isValid(); duration: {}", duration);
        return duration <= this.duration;
    }
}
