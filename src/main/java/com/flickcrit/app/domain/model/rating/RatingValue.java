package com.flickcrit.app.domain.model.rating;

/**
 * Represents a rating with a numerical value constrained within a defined range.
 * The value must be between the defined minimum and maximum limits.
 */
public record RatingValue(int value) {
    public static final int MIN_RATING = 1;
    public static final int MAX_RATING = 5;

    /**
     * Creates a new instance of Rating with the specified value.
     */
    public static RatingValue of(int value) {
        return new RatingValue(value);
    }

    /**
     * Constructs a RatingValue with a specified value.
     * Ensures the rating value falls within the allowed range.
     *
     * @param value the rating value to set; must be between MIN_RATING and MAX_RATING inclusive
     * @throws IllegalArgumentException if the value is outside the acceptable range
     */
    public RatingValue {
        if (value < MIN_RATING || value > MAX_RATING) {
            throw new IllegalArgumentException("Rating value must be between " + MIN_RATING + " and " + MAX_RATING);
        }
    }
}
