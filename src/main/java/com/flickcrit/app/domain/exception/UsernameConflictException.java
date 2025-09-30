package com.flickcrit.app.domain.exception;

/**
 * Exception thrown when attempting to create a user with a username
 * that already exists in the system.
 *
 * This runtime exception indicates a conflict in the application's domain
 * state, specifically when a user creation operation violates the
 * uniqueness constraint for usernames.
 */
public class UsernameConflictException extends EntityUniquenessException {

    private static final String ERROR_TEMPLATE = "User with username '%s' already exists.";

    public UsernameConflictException(String username) {
        super(ERROR_TEMPLATE.formatted(username));
    }

}
