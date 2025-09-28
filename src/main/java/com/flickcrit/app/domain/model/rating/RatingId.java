package com.flickcrit.app.domain.model.rating;

import com.flickcrit.app.domain.model.common.NumericEntityId;

/**
 * Represents the unique identifier for a movie rating entity.
 * This class encapsulates a numeric ID value which must
 * be positive and not null.
 */
public final class RatingId extends NumericEntityId {

    private RatingId(Long value) {
        super(value);
    }

    public static RatingId of(Long value) {
        return new RatingId(value);
    }

    public static RatingId optional(Long value) {
        return value != null ? new RatingId(value) : null;
    }
}
