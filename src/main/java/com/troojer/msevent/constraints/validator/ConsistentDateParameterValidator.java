package com.troojer.msevent.constraints.validator;

import com.troojer.msevent.constraints.ConsistentDateParameters;
import com.troojer.msevent.model.EventDateDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ConsistentDateParameterValidator
        implements ConstraintValidator<ConsistentDateParameters, EventDateDto> {

    @Override
    public boolean isValid(EventDateDto eventDateDto, ConstraintValidatorContext context) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
        ZonedDateTime startDate;
        ZonedDateTime endDate = null;
        try {
            startDate = ZonedDateTime.parse(eventDateDto.getStart(), dateTimeFormatter);
            if (eventDateDto.getEnd() != null) endDate = ZonedDateTime.parse(eventDateDto.getEnd(), dateTimeFormatter);
        } catch (Exception e) {
            return false;
        }
        ZonedDateTime now = ZonedDateTime.now(startDate.getZone());
        return startDate.isAfter(now)
                && (endDate == null || endDate.isAfter(startDate));
    }
}
