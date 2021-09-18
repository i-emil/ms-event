package com.troojer.msevent.constraints.validator;

import com.troojer.msevent.constraints.ConsistentEventStart;
import com.troojer.msevent.util.AccessCheckerUtil;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
public class EventStartValidator
        implements ConstraintValidator<ConsistentEventStart, String> {

    @Override
    public boolean isValid(String startDateStr, ConstraintValidatorContext context) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
        ZonedDateTime startDate;
        try {
            startDate = ZonedDateTime.parse(startDateStr, dateTimeFormatter);
        } catch (Exception e) {
            return false;
        }
        return startDate.toLocalDateTime().compareTo(LocalDateTime.now()) >= 0;
    }
}
