package com.flickcrit.app.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;

import java.util.Arrays;
import java.util.List;

/**
 * Validator for the {@link ValidPassword} annotation, ensuring that a given password
 * string adheres to specific rules defined for validity.
 *
 * The password is considered valid if it meets all the validation rules specified
 * in the static {@code VALIDATION_RULES} list. This includes checks such as:
 * - Length: The password must be between 8 and 30 characters.
 * - Whitespace: The password must not contain any whitespace characters.
 *
 * A null password is considered invalid.
 *
 * If the password fails validation, a descriptive error message is generated
 * and provided in the constraint violation context.
 */
class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

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
