package com.troojer.msevent.constraints.validator;

import com.troojer.msevent.client.ProfileClient;
import com.troojer.msevent.constraints.ConsistentAgeParameters;
import com.troojer.msevent.model.AgeDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ConsistentAgeParameterValidator
        implements ConstraintValidator<ConsistentAgeParameters, AgeDto> {

    private final ProfileClient profileClient;
    private int min;
    private int max;
    private int current;

    public ConsistentAgeParameterValidator(ProfileClient profileClient) {
        this.profileClient = profileClient;
    }

    @Override
    public void initialize(ConsistentAgeParameters constraintAnnotation) {
        this.current = profileClient.getProfileFilter().getAge().getCurrent();
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(AgeDto ageDto, ConstraintValidatorContext context) {
        Integer minAge = ageDto.getMin();
        Integer maxAge = ageDto.getMax();
        return minAge != null && maxAge != null &&
                minAge <= maxAge &&
                minAge >= min && maxAge <= max &&
                current >= minAge && current <= maxAge;
    }
}
