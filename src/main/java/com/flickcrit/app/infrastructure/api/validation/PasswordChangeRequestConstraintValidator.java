package com.flickcrit.app.infrastructure.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class PasswordChangeRequestConstraintValidator
    implements ConstraintValidator<ValidPasswordChangeRequest, PasswordsChangeRequest> {

    @Override
    public boolean isValid(PasswordsChangeRequest value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        boolean passwordsEquals = Objects.equals(value.getNewPassword(), value.getNewPasswordRepeat());
        if (!passwordsEquals) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Passwords do not match").addConstraintViolation();
        }

        return passwordsEquals;
    }
}
