package com.flickcrit.app.domain.model.movie;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovieIdTest {

    @Test
    void givenValidValueWhenOfCalledThenMovieIdCreated() {
        // given
        Long validValue = 1L;

        // when
        MovieId movieId = MovieId.of(validValue);

        // then
        assertNotNull(movieId, "MovieId should not be null");
        assertEquals(validValue, movieId.value(), "MovieId should hold the correct value");
    }

    @Test
    void givenNullValueWhenOfCalledThenExceptionThrown() {
        // given
        Long nullValue = null;

        // when & then
        assertThrows(IllegalArgumentException.class, () -> MovieId.of(nullValue));
    }
}