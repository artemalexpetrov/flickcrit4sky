package com.flickcrit.app.infrastructure.persistence.converter;

import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.domain.model.rating.Rating;
import com.flickcrit.app.domain.model.rating.RatingId;
import com.flickcrit.app.domain.model.rating.RatingValue;
import com.flickcrit.app.domain.model.user.UserId;
import com.flickcrit.app.infrastructure.persistence.model.RatingEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RatingToRatingEntityConverterTest {

    private final RatingToRatingEntityConverter converter
        = new RatingToRatingEntityConverter();

    @Test
    void givenRatingWithIdWhenConvertExpectRatingEntity() {
        // given
        Rating rating = getRatingBuilder().build();

        // when
        RatingEntity result = converter.convert(rating);

        // then
        assertNotNull(result);
        assertEquals(rating.getId().value(), result.getId());
        assertEquals(rating.getUserId().value(), result.getUserId());
        assertEquals(rating.getMovieId().value(), result.getMovieId());
        assertEquals(rating.getValue().value(), result.getRating());
        assertEquals(rating.getCreatedAt(), result.getCreatedAt());
        assertEquals(rating.getUpdatedAt(), result.getUpdatedAt());
    }

    @Test
    void givenRatingWithoutIdWhenConvertExpectRatingEntity() {
        // given
        Rating rating = getRatingBuilder().id(null).build();

        // when
        RatingEntity result = converter.convert(rating);

        // then
        assertNotNull(result);
        assertNull(result.getId());
    }

    private static Rating.RatingBuilder getRatingBuilder() {
        return Rating.builder()
            .id(RatingId.of(1L))
            .userId(UserId.of(2L))
            .movieId(MovieId.of(3L))
            .value(RatingValue.of(4))
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now().plusSeconds(1));
    }
}