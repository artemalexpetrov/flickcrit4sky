package com.flickcrit.app.domain.model.movie;

import com.flickcrit.app.domain.model.common.NumericEntityId;

/**
 * Represents the unique identifier for a movie entity.
 * This class encapsulates a numeric ID value which must
 * be positive and not null.
 */
public final class MovieId extends NumericEntityId {

    private MovieId(Long value) {
        super(value);
    }

    public static MovieId of(Long value) {
        return new MovieId(value);
    }

    public static MovieId optional(Long value) {
        return value != null ? new MovieId(value) : null;
    }
}
