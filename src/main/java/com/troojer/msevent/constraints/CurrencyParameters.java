package com.troojer.msevent.constraints;

import com.troojer.msevent.constraints.validator.BudgetValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = BudgetValidator.class)
@Target({TYPE, FIELD})
@Retention(RUNTIME)
@Documented
public @interface CurrencyParameters {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String param();

    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        CurrencyParameters[] value();
    }
}