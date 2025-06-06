package com.troojer.msevent.constraints;

import com.troojer.msevent.constraints.validator.EventDurationValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = EventDurationValidator.class)
@Target({TYPE, FIELD})
@Retention(RUNTIME)
@Documented
public @interface ConsistentEventDuration {

    String message() default
            "duration is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ConsistentEventDuration[] value();
    }
    String param();
}