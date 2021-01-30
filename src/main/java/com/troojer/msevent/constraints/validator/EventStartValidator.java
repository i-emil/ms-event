package com.troojer.msevent.constraints.validator;

import com.troojer.msevent.client.UserPlanConstantsClient;
import com.troojer.msevent.constraints.ConsistentEventStart;
import com.troojer.msevent.model.StartEndDatesDto;
import com.troojer.msevent.util.AccessCheckerUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class EventStartValidator
        implements ConstraintValidator<ConsistentEventStart, StartEndDatesDto> {

    private final AccessCheckerUtil accessChecker;
    private Duration maxPeriodBeforeStarting;
    private Duration minPeriodBeforeStarting;

    public EventStartValidator(AccessCheckerUtil accessChecker) {
        this.accessChecker = accessChecker;
    }

    @Override
    public void initialize(ConsistentEventStart constraintAnnotation) {
        this.maxPeriodBeforeStarting = Duration.ofDays(UserPlanConstantsClient.getMaxDaysBeforeStarting(accessChecker.getPlan()));
        this.minPeriodBeforeStarting = Duration.ofMinutes(UserPlanConstantsClient.getMinMinutesBeforeStarting());
    }

    @Override
    public boolean isValid(StartEndDatesDto startEndDatesDto, ConstraintValidatorContext context) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
        ZonedDateTime startDate;
        try {
            startDate = ZonedDateTime.parse(startEndDatesDto.getStart(), dateTimeFormatter);
        } catch (Exception e) {
            return false;
        }
        return startDate.toLocalDate().compareTo(LocalDate.now().plus(maxPeriodBeforeStarting)) <= 0 &&
                startDate.toLocalDate().compareTo(LocalDate.now().plus(minPeriodBeforeStarting)) >= 0;
    }
}
