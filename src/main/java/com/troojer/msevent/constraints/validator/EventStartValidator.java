package com.troojer.msevent.constraints.validator;

import com.troojer.msevent.constraints.ConsistentEventStart;
import com.troojer.msevent.model.EventDateDto;
import com.troojer.msevent.client.UserPlanConstantsClient;
import com.troojer.msevent.util.AccessCheckerUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class EventStartValidator
        implements ConstraintValidator<ConsistentEventStart, EventDateDto> {

    private final AccessCheckerUtil accessChecker;
    private Period validPeriod;

    public EventStartValidator(AccessCheckerUtil accessChecker) {
        this.accessChecker = accessChecker;
    }

    @Override
    public void initialize(ConsistentEventStart constraintAnnotation) {
        this.validPeriod = Period.ofDays(UserPlanConstantsClient.getDaysBeforeStarting(accessChecker.getPlan()));
    }

    @Override
    public boolean isValid(EventDateDto eventDateDto, ConstraintValidatorContext context) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
        ZonedDateTime startDate;
        try {
            startDate = ZonedDateTime.parse(eventDateDto.getStart(), dateTimeFormatter);
        } catch (Exception e) {
            return false;
        }
        return startDate.toLocalDate().compareTo(LocalDate.now().plus(validPeriod)) <= 0;
    }
}
