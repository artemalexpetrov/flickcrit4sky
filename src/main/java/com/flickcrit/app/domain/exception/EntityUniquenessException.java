package com.flickcrit.app.domain.exception;

/**
 * Exception thrown to indicate a violation of the uniqueness constraint
 * for a specific entity in the system's domain.
 *
 * This runtime exception is typically used when an operation fails due
 * to the presence of an existing entity with the same unique identifier
 * or attribute, ensuring the integrity of unique constraints within
 * the application's domain.
 */
public class EntityUniquenessException extends RuntimeException {

    public EntityUniquenessException(String message) {
        super(message);
    }

    public EntityUniquenessException(String message, Throwable cause) {
        super(message, cause);
    }
}
