package com.troojer.msevent.constraints.validator;

import com.troojer.msevent.constraints.NullOrNotEmpty;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

public class NullOrNotEmptyValidator implements ConstraintValidator<NullOrNotEmpty, Collection> {

    public boolean isValid(Collection collection, ConstraintValidatorContext constraintValidatorContext) {
        return collection == null || !collection.isEmpty();
    }
}
