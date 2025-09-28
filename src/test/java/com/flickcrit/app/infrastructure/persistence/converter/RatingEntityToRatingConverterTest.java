package com.flickcrit.app.infrastructure.persistence.converter;

import com.flickcrit.app.domain.model.rating.Rating;
import com.flickcrit.app.infrastructure.persistence.model.RatingEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RatingEntityToRatingConverterTest {

    private final RatingEntityToRatingConverter converter 
        = new RatingEntityToRatingConverter();

    @Test
    void givenValidRatingEntityWhenConvertExpectRating() {
        // given
        LocalDateTime now = LocalDateTime.now();
        RatingEntity entity = RatingEntity.builder()
            .id(1L)
            .userId(2L)
            .movieId(3L)
            .rating(4)
            .createdAt(now)
            .updatedAt(now)
            .build();

        // when
        Rating result = converter.convert(entity);

        // then
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId().value());
        assertEquals(entity.getUserId(), result.getUserId().value());
        assertEquals(entity.getMovieId(), result.getMovieId().value());
        assertEquals(entity.getRating(), result.getValue().value());
        assertEquals(entity.getCreatedAt(), result.getCreatedAt());
        assertEquals(entity.getUpdatedAt(), result.getUpdatedAt());
    }
}