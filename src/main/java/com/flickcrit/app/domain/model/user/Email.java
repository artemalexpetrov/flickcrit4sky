package com.flickcrit.app.domain.model.user;

import java.util.regex.Pattern;

/**
 * Represents an email address in the system.
 * This class encapsulates the email value and ensures it adheres to the
 * specified format requirements.
 * <p/>
 * Validation criteria (pretty weak, didn't put much efforts into it, but good enough for the demo):
 * - The email address must be between 4 and 40 characters long.
 * - The email address must match a standard email pattern, containing
 *   valid characters and a domain.
 * <p/>
 * Attempts to create an instance with an invalid email address will
 * result in an {@code IllegalArgumentException}.
 */
public record Email(String value) {

    private static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

    public Email {
        validateValue(value);
    }

    public static Email of(String value) {
        return new Email(value);
    }

    private static void validateValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Email value must not be null");
        }

        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid email address");
        }
    }
}
