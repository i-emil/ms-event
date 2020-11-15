package com.troojer.msevent.constraints.validator;

import com.troojer.msevent.constraints.ConsistentAgeParameters;
import com.troojer.msevent.model.AgeDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ConsistentAgeParameterValidator
        implements ConstraintValidator<ConsistentAgeParameters, AgeDto> {

    private int min;
    private int max;

    @Override
    public void initialize(ConsistentAgeParameters constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(AgeDto ageDto, ConstraintValidatorContext context) {
        Integer minAge = ageDto.getMin();
        Integer maxAge = ageDto.getMax();
        return minAge != null && maxAge != null && minAge <= maxAge
                && minAge >= min && maxAge <= max ;
    }
}
