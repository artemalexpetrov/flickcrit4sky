package com.flickcrit.app.domain.model.user;

import com.flickcrit.app.domain.model.common.NumericEntityId;

/**
 * Represents the unique identifier for a user entity.
 * This class encapsulates a numeric ID value which must
 * be positive and not null.
 */
public final class UserId extends NumericEntityId {

    private UserId(Long value) {
        super(value);
    }

    public static UserId of(Long value) {
        return new UserId(value);
    }

    public static UserId optional(Long value) {
        return value != null ? new UserId(value) : null;
    }
}
