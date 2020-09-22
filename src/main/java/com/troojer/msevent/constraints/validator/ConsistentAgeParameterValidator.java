package com.troojer.msevent.constraints.validator;

import com.troojer.msevent.constraints.ConsistentAgeParameters;
import com.troojer.msevent.model.EventAgeDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ConsistentAgeParameterValidator
        implements ConstraintValidator<ConsistentAgeParameters, EventAgeDto> {

    private int min;
    private int max;

    @Override
    public void initialize(ConsistentAgeParameters constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(EventAgeDto eventAgeDto, ConstraintValidatorContext context) {
        Integer minAge = eventAgeDto.getMin();
        Integer maxAge = eventAgeDto.getMax();
        return minAge != null && maxAge != null && minAge <= maxAge
                && minAge >= min && maxAge <= max ;
    }
}
