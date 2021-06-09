package com.troojer.msevent.constraints.validator;

import com.troojer.msevent.constraints.ConsistentEventDuration;
import com.troojer.msevent.model.enm.Duration;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class EventDurationValidator
        implements ConstraintValidator<ConsistentEventDuration, Integer> {

    @Override
    public boolean isValid(Integer duration, ConstraintValidatorContext context) {
        return duration == null || Arrays.stream(Duration.values()).anyMatch(x -> x.getHours() == duration);
    }
}
