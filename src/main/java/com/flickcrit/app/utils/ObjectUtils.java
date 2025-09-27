package com.flickcrit.app.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ObjectUtils {

    /**
     * Ensures that the provided object is not null.
     *
     * @param obj the object to check for nullity
     * @param message the exception message to use if the object is null
     * @return the non-null object passed as the parameter
     * @throws IllegalArgumentException if the object is null
     */
    public static <T> T requireNonNull(T obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }

        return obj;
    }
}
