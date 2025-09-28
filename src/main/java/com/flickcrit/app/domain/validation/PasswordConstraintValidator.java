package com.flickcrit.app.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;

import java.util.Arrays;
import java.util.List;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    private static final List<Rule> VALIDATION_RULES = Arrays.asList(
        new LengthRule(8, 30),
        new WhitespaceRule());

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        PasswordValidator passwordValidator = new PasswordValidator(VALIDATION_RULES);
        RuleResult result = passwordValidator.validate(new PasswordData(value));
        if (result.isValid()) {
            return true;
        }

        String errorMessage = String.join(", ", passwordValidator.getMessages(result));
        context.disableDefaultConstraintViolation();
        context
            .buildConstraintViolationWithTemplate(errorMessage)
            .addConstraintViolation();

        return false;
    }
}
