package com.flickcrit.app.infrastructure.persistence.converter;

import com.flickcrit.app.domain.model.movie.Movie;
import com.flickcrit.app.infrastructure.persistence.model.MovieEntity;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MovieEntityToMovieConverterTest {

    private final MovieEntityToMovieConverter converter = new MovieEntityToMovieConverter();

    @Test
    void givenMovieEntityWhenConvertExpectMovie() {
        // given
        MovieEntity entity = MovieEntity.builder()
            .id(1L)
            .isan(UUID.randomUUID())
            .title("Test Movie")
            .year(Year.of(2023))
            .build();

        // when
        Movie movie = converter.convert(entity);

        // then
        assertThat(movie).isNotNull();
        assertThat(movie.getId().value()).isEqualTo(entity.getId());
        assertThat(movie.getIsan().value()).isEqualTo(entity.getIsan());
        assertThat(movie.getTitle()).isEqualTo(entity.getTitle());
        assertThat(movie.getYear()).isEqualTo(entity.getYear());
    }
}