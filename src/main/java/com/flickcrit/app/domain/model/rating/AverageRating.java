package com.flickcrit.app.domain.model.rating;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents the average rating for a collection of ratings.
 * Encapsulates the average value and the total count of ratings.
 * Ensures that the average rating value is within valid bounds and rounded to
 * a specified precision.
 */
public record AverageRating(BigDecimal value, long ratesCount) {

    private static final int AVERAGE_RATING_SCALE = 1;
    private static final BigDecimal MIN_AVERAGE_RATING = BigDecimal.ZERO;
    private static final BigDecimal MAX_AVERAGE_RATING = BigDecimal.valueOf(RatingValue.MAX_RATING);

    /**
     * Constructs an AverageRating with a specified value.
     * The value must be within the allowed range and will be scaled to defined precision.
     *
     * @param value the average rating value to assign; must be between MIN_AVERAGE_RATING (inclusive)
     *              and MAX_AVERAGE_RATING (inclusive), scaled to AVERAGE_RATING_SCALE
     *
     * @throws IllegalArgumentException if the value is outside the acceptable range
     */
    public AverageRating {
        if (value.compareTo(MIN_AVERAGE_RATING) < 0 || value.compareTo(MAX_AVERAGE_RATING) > 0) {
            throw new IllegalArgumentException("Average rating must be within the range [%f, %02f]".formatted(
                MIN_AVERAGE_RATING, MAX_AVERAGE_RATING
            ));
        }

        value = value.setScale(AVERAGE_RATING_SCALE, RoundingMode.HALF_UP);
    }

    public AverageRating(Double value, long ratesCount) {
        this(BigDecimal.valueOf(value), ratesCount);
    }

    public static AverageRating of(BigDecimal value, long ratesCount) {
        return new AverageRating(value, ratesCount);
    }

    public static AverageRating zero() {
        return new AverageRating(BigDecimal.ZERO, 0);
    }
}
