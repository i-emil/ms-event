package com.troojer.msevent.constraints;

import com.troojer.msevent.constraints.validator.ConsistentPersonCountValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = ConsistentPersonCountValidator.class)
@Target({TYPE, FIELD})
@Retention(RUNTIME)
@Documented
public @interface ConsistentPersonCountParameters {

    String message() default
            "Person count is wrong";

    int min() default 1;

    int max() default 10;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String param();

    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ConsistentPersonCountParameters[] value();
    }
}