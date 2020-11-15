package com.troojer.msevent.constraints.validator;

import com.troojer.msevent.constraints.ConsistentNumberRange;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ConsistentNumberRangeValidator
        implements ConstraintValidator<ConsistentNumberRange, Integer> {

    private int min;
    private int max;

    @Override
    public void initialize(ConsistentNumberRange constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Integer number, ConstraintValidatorContext context) {
        return number >= min && number <= max;
    }
}
