package com.flickcrit.app.infrastructure.persistence.converter;

import com.flickcrit.app.domain.model.movie.Isan;
import com.flickcrit.app.domain.model.movie.Movie;
import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.infrastructure.persistence.model.MovieEntity;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class MovieToMovieEntityConverterTest {

    private final MovieToMovieEntityConverter converter = new MovieToMovieEntityConverter();

    @Test
    void givenMovieWhenConvertExpectMovieEntity() {
        // given
        Movie movie = createMovieBuilder().build();

        // when
        MovieEntity movieEntity = converter.convert(movie);

        // then
        assertNotNull(movieEntity);
        assertEquals(movie.getId().value(), movieEntity.getId());
        assertEquals(movie.getIsan().value(), movieEntity.getIsan());
        assertEquals(movie.getTitle(), movieEntity.getTitle());
        assertEquals(movie.getYear(), movieEntity.getYear());
    }

    @Test
    void givenMovieWithoutId_whenConvert_thenReturnsMovieEntityWithoutId() {
        // given
        Movie movie = createMovieBuilder().id(null).build();

        // when
        MovieEntity movieEntity = converter.convert(movie);

        // then
        assertNotNull(movieEntity);
        assertNull(movieEntity.getId());
    }

    private static Movie.MovieBuilder createMovieBuilder() {
        return Movie.builder()
            .id(MovieId.of(1L))
            .isan(Isan.generate())
            .title("Inception")
            .year(Year.of(2010));
    }
}