package com.flickcrit.app.infrastructure.api.port.impl;


import com.flickcrit.app.domain.model.movie.Movie;
import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.domain.model.rating.AverageRating;
import com.flickcrit.app.domain.model.rating.RatedMovieId;
import com.flickcrit.app.domain.repository.MovieRepository;
import com.flickcrit.app.domain.repository.RatingRepository;
import com.flickcrit.app.infrastructure.api.model.movie.MovieDto;
import com.flickcrit.app.infrastructure.api.model.rating.AverageRatingDto;
import com.flickcrit.app.infrastructure.api.model.rating.RatedMovieDto;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopRatingPortImplTest {

    private static final int TOP_RATING_COUNT = 10;

    @Mock
    private RatingRepository ratingRepositoryMock;

    @Mock
    private MovieRepository movieRepositoryMock;

    @Mock
    private ConversionService converterMock;

    @InjectMocks
    private TopRatingPortImpl topRatingPort;


    @Test
    void givenNoTopRatedMoviesWhenGetTopRatedMoviesExpectEmptyList() {
        // given
        int topRatedMovieCount = 10;
        when(ratingRepositoryMock
            .getTopRatedMovies(topRatedMovieCount))
            .thenReturn(List.of());

        // when
        List<RatedMovieDto> result = topRatingPort.getTopRatedMovies();

        assertThat(result).isEmpty();
        verify(ratingRepositoryMock).getTopRatedMovies(topRatedMovieCount);
        verifyNoMoreInteractions(ratingRepositoryMock);
        verifyNoInteractions(movieRepositoryMock, converterMock);
    }

    @Test
    void givenRatedMoviesWhenGetTopRatedMoviesExpectTopRatedMovies() {
        // given
        List<MovieId> movieIds = List.of(MovieId.of(10L), MovieId.of(20L));

        List<AverageRating> ratings = List.of(
            AverageRating.of(BigDecimal.valueOf(5.0d), 10),
            AverageRating.of(BigDecimal.valueOf(5.0d), 20)
        );

        List<Movie> movies = movieIds.stream()
            .map(TopRatingPortImplTest::mockMovie)
            .toList();

        List<MovieDto> moviesDto = List.of(
            mock(MovieDto.class),
            mock(MovieDto.class)
        );

        List<AverageRatingDto> ratingsDto = List.of(
            new AverageRatingDto(BigDecimal.valueOf(5.0d), 10L),
            new AverageRatingDto(BigDecimal.valueOf(5.0d), 20L)
        );

        List<RatedMovieId> ratedMovieIds = IntStream.range(0, movieIds.size())
            .mapToObj(i -> new RatedMovieId(movieIds.get(i), ratings.get(i)))
            .toList();

        List<RatedMovieDto> expectedMovies = IntStream.range(0, movieIds.size())
            .mapToObj(i -> new RatedMovieDto(moviesDto.get(i), ratingsDto.get(i)))
            .toList();

        when(ratingRepositoryMock
            .getTopRatedMovies(anyInt()))
            .thenReturn(ratedMovieIds);

        when(movieRepositoryMock
            .findByIds(anyCollection()))
            .thenReturn(movies);

        when(converterMock
            .convert(any(Movie.class), eq(MovieDto.class)))
            .thenReturn(moviesDto.get(0), moviesDto.get(1));

        // when
        List<RatedMovieDto> topRatedMovies = topRatingPort.getTopRatedMovies();

        // then
        assertThat(topRatedMovies).containsExactlyElementsOf(expectedMovies);
        verify(ratingRepositoryMock).getTopRatedMovies(TOP_RATING_COUNT);
        verify(movieRepositoryMock).findByIds(movieIds);
        verify(converterMock, times(2)).convert(any(Movie.class), eq(MovieDto.class));
        verifyNoMoreInteractions(ratingRepositoryMock, movieRepositoryMock, converterMock);
    }

    private static @NotNull Movie mockMovie(MovieId id) {
        Movie movieMock = mock(Movie.class);
        when(movieMock.getId()).thenReturn(id);
        return movieMock;
    }
}