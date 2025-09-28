package com.flickcrit.app.infrastructure.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Constraint(validatedBy = PasswordChangeRequestConstraintValidator.class)
public @interface ValidPasswordChangeRequest {

    String message() default "Invalid Password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
