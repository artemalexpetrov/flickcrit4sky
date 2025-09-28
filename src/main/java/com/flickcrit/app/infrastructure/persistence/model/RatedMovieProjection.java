package com.flickcrit.app.infrastructure.persistence.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents a projection of a rated movie containing the movie's ID, average rating, and the total number of ratings.
 * This is used primarily as a data transfer object for querying rated movie information.
 *
 * The {@code rating} property is automatically rounded to two decimal places using HALF_UP rounding mode.
 * If a {@code null} value is provided for {@code rating}, it defaults to {@link BigDecimal#ZERO}.
 *
 * This record is useful for scenarios where summarized information about a movie's rating is required,
 * especially for display purposes like leaderboards or top-rated lists.
 *
 * @param movieId the unique identifier of the movie
 * @param rating the average rating of the movie, rounded to two decimal places
 * @param ratesCount the total number of ratings the movie has received
 */
public record RatedMovieProjection(Long movieId, BigDecimal rating, Long ratesCount) {

    public static final int DEFAULT_RATING_SCALE = 1;

    public RatedMovieProjection {
        rating  = rating != null
            ? rating.setScale(DEFAULT_RATING_SCALE, RoundingMode.HALF_UP)
            : BigDecimal.ZERO;
    }
}
