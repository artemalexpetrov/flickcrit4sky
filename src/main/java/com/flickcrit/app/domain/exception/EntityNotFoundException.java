package com.flickcrit.app.domain.exception;

/**
 * Exception thrown when an entity cannot be found in the system.
 * This runtime exception is typically used in scenarios where a requested
 * entity is expected to exist but does not, indicating an operational issue.
 */
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }
}
