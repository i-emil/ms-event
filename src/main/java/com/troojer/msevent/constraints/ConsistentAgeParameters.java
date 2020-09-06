package com.troojer.msevent.constraints;

import com.troojer.msevent.constraints.validator.ConsistentAgeParameterValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = ConsistentAgeParameterValidator.class)
@Target({TYPE, FIELD})
@Retention(RUNTIME)
@Documented
public @interface ConsistentAgeParameters {

    String message() default
            "End date must be after begin date and both must be in the future";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String param();

    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ConsistentAgeParameters[] value();
    }
}