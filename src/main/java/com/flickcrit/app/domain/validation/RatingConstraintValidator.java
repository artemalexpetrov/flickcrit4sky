package com.flickcrit.app.domain.validation;

import com.flickcrit.app.domain.model.rating.RatingValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for the {@link ValidRating} annotation, ensuring that a given int value is within
 * the allowed rating range defined by {@link RatingValue#MIN_RATING} and {@link RatingValue#MAX_RATING}.
 * <p/>
 * A null value is considered valid. If the value is outside the allowed range, a custom constraint
 * violation message is provided.
 */
class RatingConstraintValidator implements ConstraintValidator<ValidRating, Integer> {

    private static final String VIOLATION_TEMPLATE = "Rating value must be between %d and %d";

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (value < RatingValue.MIN_RATING || value > RatingValue.MAX_RATING) {
            buildConstraintViolation(context);
            return false;
        }

        return true;
    }

    private void buildConstraintViolation(ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context
            .buildConstraintViolationWithTemplate(VIOLATION_TEMPLATE
                .formatted(RatingValue.MIN_RATING, RatingValue.MAX_RATING))
            .addConstraintViolation();
    }
}
