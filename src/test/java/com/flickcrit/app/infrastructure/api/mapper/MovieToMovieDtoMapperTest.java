package com.flickcrit.app.infrastructure.api.mapper;


import com.flickcrit.app.domain.model.movie.Isan;
import com.flickcrit.app.domain.model.movie.Movie;
import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.infrastructure.api.model.movie.MovieDto;
import com.flickcrit.app.infrastructure.core.conversion.mapper.EntityIdMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.assertj.core.api.Assertions.assertThat;

class MovieToMovieDtoMapperTest {

    private MovieToMovieDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new MovieToMovieDtoMapperImpl(new EntityIdMapper());
    }

    @Test
    void givenAMovieWhenConvertToDtoExpectDtoWithSameValues() {
        // given
        Movie movie = Movie.builder()
            .id(MovieId.of(1L))
            .title("Test Movie")
            .isan(Isan.generate())
            .year(Year.of(2025))
            .build();

        // when
        MovieDto result = mapper.convert(movie);

        // then
        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo(movie.getTitle());
        assertThat(result.isan()).isEqualTo(movie.getIsan().value());
        assertThat(result.year()).isEqualTo(movie.getYear().getValue());
    }
}