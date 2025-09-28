package com.flickcrit.app.infrastructure.persistence.converter;

import com.flickcrit.app.domain.model.rating.RatedMovieId;
import com.flickcrit.app.infrastructure.persistence.model.RatedMovieProjection;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RatedMovieProjectionToRatedMovieIdConverterTest {

    private final RatedMovieProjectionToRatedMovieIdConverter converter
        = new RatedMovieProjectionToRatedMovieIdConverter();

    @Test
    void shouldConvertValidProjection() {
        // given
        RatedMovieProjection projection = new RatedMovieProjection(10L, BigDecimal.ONE, 10L);

        // when
        RatedMovieId result = converter.convert(projection);

        // then
        assertNotNull(result);
        assertEquals(projection.movieId(), result.movieId().value());
        assertEquals(projection.rating(), result.rating().value());
        assertEquals(projection.ratesCount(), result.rating().ratesCount());
    }
}