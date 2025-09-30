package com.flickcrit.app.infrastructure.persistence.model;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RatedMovieProjectionTest {

    @Test
    void givenNonNullRatingWhenProjectionCreatedExpectPrecisionSet() {
        // given
        BigDecimal rating = BigDecimal.valueOf(8.5785);
        BigDecimal expectedScaledRating = rating.setScale(1, RoundingMode.HALF_UP);

        // when
        RatedMovieProjection projection = new RatedMovieProjection(1L, rating, 10L);

        // then
        assertEquals(expectedScaledRating, projection.rating());
    }

    @Test
    void givenNullRatingWhenProjectionCreatedExpectZeroAndPrecisionSet() {
        // given
        BigDecimal expectedScaledRating = BigDecimal.ZERO.setScale(1, RoundingMode.HALF_UP);

        // when
        RatedMovieProjection projection = new RatedMovieProjection(1L, null, 10L);

        // then
        assertEquals(expectedScaledRating, projection.rating());
    }
}