package com.flickcrit.app.domain.model.movie;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class MovieTest {

    @Test
    void givenMovieTitleAndYearWhenMovieCreatedExpectIsanGenerated() {
        // given
        String title = "Test Movie";
        Year year = Year.of(2020);

        // when
        Movie movie = new Movie(title, year);

        // then
        assertNotNull(movie.getIsan());
        assertNotNull(movie.getIsan().value());
    }

    @Test
    void givenValidTitleWhenSetTitleThenTitleIsSet() {
        // given
        String validTitle = "Inception";
        Movie movie = new Movie("Initial Title", Year.of(2010));

        // when
        Movie result = movie.setTitle(validTitle);

        // then
        assertEquals(validTitle, movie.getTitle());
        assertSame(movie, result);
    }

    @Test
    void givenValidTitleWhenSetTitleExpectTitleTrimmed() {
        // given
        String untrimmedTitle = "  The Matrix  ";
        String expectedTitle = "The Matrix";
        Movie movie = new Movie("Initial Title", Year.of(2010));

        // when
        Movie result = movie.setTitle(untrimmedTitle);

        // then
        assertEquals(expectedTitle, movie.getTitle());
        assertSame(movie, result);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    void givenInvalidTitleWhenSetTitleThenThrowsIllegalArgumentException(String invalidTitle) {
        // given
        Movie movie = new Movie("Initial Title", Year.of(2015));

        // when / then
        assertThrows(IllegalArgumentException.class, () -> movie.setTitle(invalidTitle));
    }

    @Test
    void givenValidYearWhenSetYearThenYearIsSet() {
        // given
        Year validYear = Year.of(2020);
        Movie movie = new Movie("Test Movie", Year.of(2010));

        // when
        Movie result = movie.setYear(validYear);

        // then
        assertEquals(validYear, movie.getYear());
        assertSame(movie, result);
    }

    @Test
    void givenNullYearWhenSetYearThenThrowsIllegalArgumentException() {
        // given
        Movie movie = new Movie("Test Movie", Year.of(2015));

        // when / then
        assertThrows(IllegalArgumentException.class, () -> movie.setYear(null));
    }
}