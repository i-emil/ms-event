package com.troojer.msevent.constraints.validator;

import com.troojer.msevent.client.UserPlanClient;
import com.troojer.msevent.constraints.ConsistentEventStart;
import com.troojer.msevent.util.AccessCheckerUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class EventStartValidator
        implements ConstraintValidator<ConsistentEventStart, String> {

    private final AccessCheckerUtil accessChecker;
    private final UserPlanClient userPlanClient;
    private Duration maxPeriodBeforeStarting; //remove max period at all. maybe i'll return it in future releases
    private Duration minPeriodBeforeStarting;

    public EventStartValidator(AccessCheckerUtil accessChecker, UserPlanClient userPlanClient) {
        this.accessChecker = accessChecker;
        this.userPlanClient = userPlanClient;
    }

    @Override
    public void initialize(ConsistentEventStart constraintAnnotation) {
        this.maxPeriodBeforeStarting = Duration.ofDays(userPlanClient.getPermitValue(accessChecker.getPlan(), "EVENT_MAX_DAYS_BEFORE_START"));
        this.minPeriodBeforeStarting = Duration.ofMinutes(userPlanClient.getPermitValue(accessChecker.getPlan(), "EVENT_MIN_MINUTES_BEFORE_START"));
    }

    @Override
    public boolean isValid(String startDateStr, ConstraintValidatorContext context) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
        ZonedDateTime startDate;
        try {
            startDate = ZonedDateTime.parse(startDateStr, dateTimeFormatter);
        } catch (Exception e) {
            return false;
        }
        return startDate.toLocalDateTime().compareTo(LocalDateTime.now().plus(minPeriodBeforeStarting)) >= 0;
    }
}
