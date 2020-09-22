package com.troojer.msevent.constraints.validator;

import com.troojer.msevent.constraints.ConsistentPersonCountParameters;
import com.troojer.msevent.model.EventPersonCountDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ConsistentPersonCountValidator
        implements ConstraintValidator<ConsistentPersonCountParameters, EventPersonCountDto> {

    private int min;
    private int max;

    @Override
    public void initialize(ConsistentPersonCountParameters constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(EventPersonCountDto eventPersonCountDto, ConstraintValidatorContext context) {
        int count = eventPersonCountDto.getMaleCount() + eventPersonCountDto.getFemaleCount() + eventPersonCountDto.getAllCount();
        return count > min && count <= max;
    }
}
