package com.troojer.msevent.constraints;

import com.troojer.msevent.constraints.validator.ConsistentNumberRangeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = ConsistentNumberRangeValidator.class)
@Target({TYPE, FIELD})
@Retention(RUNTIME)
@Documented
public @interface ConsistentNumberRange {

    String message() default
            "Number have to be between {min} and {max}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int min();

    int max();

    String param();

    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ConsistentNumberRange[] value();
    }
}