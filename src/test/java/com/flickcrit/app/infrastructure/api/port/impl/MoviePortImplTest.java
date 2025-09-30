package com.flickcrit.app.infrastructure.api.port.impl;

import com.flickcrit.app.domain.exception.EntityNotFoundException;
import com.flickcrit.app.domain.model.movie.Movie;
import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.domain.service.MovieService;
import com.flickcrit.app.infrastructure.api.model.common.PageRequestDto;
import com.flickcrit.app.infrastructure.api.model.common.PageResponse;
import com.flickcrit.app.infrastructure.api.model.movie.MovieCreateRequest;
import com.flickcrit.app.infrastructure.api.model.movie.MovieDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.Year;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MoviePortImplTest {
    
    @Mock
    private MovieService movieServiceMock;

    @Mock
    private ConversionService converterMock;

    @InjectMocks
    private MoviePortImpl moviePort;

    @Test
    void givenPageableRequestWhenGetMoviesExpectPageResponse() {
        // given
        Movie movie = mock(Movie.class);
        MovieDto movieDto = mock(MovieDto.class);
        PageRequestDto pageRequest = PageRequestDto.of(10, 40);
        Page<Movie> moviePage = new PageImpl<>(List.of(movie));

        when(movieServiceMock
            .getMovies(any()))
            .thenReturn(moviePage);

        when(converterMock
            .convert(any(), eq(MovieDto.class)))
            .thenReturn(movieDto);

        // when
        PageResponse<MovieDto> moviesDto = moviePort.getMovies(pageRequest);

        // then
        assertThat(moviesDto).isNotNull();
        assertThat(moviesDto.getItems()).containsExactly(movieDto);
        verify(movieServiceMock).getMovies(assertArg(pageable -> {
            assertEquals(pageRequest.getPage(), pageable.getPageNumber());
            assertEquals(pageRequest.getSize(), pageable.getPageSize());
        }));
        verify(converterMock).convert(movie, MovieDto.class);
        verifyNoMoreInteractions(movieServiceMock, converterMock);
    }

    @Test
    void givenMovieWhenGetMovieExpectMovieDto() {
        // given
        MovieId movieId = MovieId.of(15L);
        Movie movie = mock(Movie.class);
        MovieDto movieDto = mock(MovieDto.class);

        when(movieServiceMock
            .getMovie(any()))
            .thenReturn(movie);

        when(converterMock
            .convert(any(), eq(MovieDto.class)))
            .thenReturn(movieDto);

        // when
        MovieDto result = moviePort.getMovie(movieId);

        // then
        assertThat(result).isEqualTo(movieDto);
        verify(movieServiceMock).getMovie(movieId);
        verify(converterMock).convert(movie, MovieDto.class);
        verifyNoMoreInteractions(movieServiceMock, converterMock);
    }

    @Test
    void givenNonExistentMovieWhenGetMovieExpectEntityNotFoundException() {
        // given
        MovieId movieId = MovieId.of(99L);
        when(movieServiceMock.getMovie(any()))
            .thenThrow(new EntityNotFoundException("Movie not found"));

        // when/then
        assertThrows(EntityNotFoundException.class, () -> moviePort.getMovie(movieId));
        verify(movieServiceMock).getMovie(movieId);
        verifyNoMoreInteractions(movieServiceMock);
        verifyNoInteractions(converterMock);
    }

    @Test
    void givenValidRequestWhenCreateMovieExpectMovieDto() {
        // given
        MovieCreateRequest request = new MovieCreateRequest("Test Movie", 2025);

        Movie savedMovie = mock(Movie.class);
        MovieDto movieDto = mock(MovieDto.class);

        when(movieServiceMock
            .save(any(Movie.class)))
            .thenReturn(savedMovie);

        when(converterMock
            .convert(any(), eq(MovieDto.class)))
            .thenReturn(movieDto);

        // when
        MovieDto result = moviePort.createMovie(request);

        // then
        assertThat(result).isEqualTo(movieDto);
        verify(movieServiceMock).save(assertArg(movie -> {
            assertEquals(request.title(), movie.getTitle());
            assertEquals(request.year(), movie.getYear().getValue());
        }));

        verify(converterMock).convert(savedMovie, MovieDto.class);
        verifyNoMoreInteractions(movieServiceMock, converterMock);
    }

    @Test
    void givenValidRequestWhenUpdateMovieExpectUpdatedMovieDto() {
        // given
        MovieId movieId = MovieId.of(1L);
        MovieCreateRequest request = new MovieCreateRequest("Updated Movie", 2026);
        Movie existingMovie = new Movie("Initial Title", Year.of(2025));

        Movie savedMovie = mock(Movie.class);
        MovieDto movieDto = mock(MovieDto.class);

        when(movieServiceMock
            .getMovie(movieId))
            .thenReturn(existingMovie);

        when(movieServiceMock
            .save(existingMovie))
            .thenReturn(savedMovie);

        when(converterMock
            .convert(savedMovie, MovieDto.class))
            .thenReturn(movieDto);

        // when
        MovieDto result = moviePort.updateMovie(movieId, request);

        // then
        assertThat(result).isEqualTo(movieDto);
        verify(movieServiceMock).getMovie(movieId);
        verify(movieServiceMock).save(assertArg(updatedMovie -> {
            assertEquals(request.title(), updatedMovie.getTitle());
            assertEquals(request.year(), updatedMovie.getYear().getValue());
        }));
        
        verify(converterMock).convert(savedMovie, MovieDto.class);
        verifyNoMoreInteractions(movieServiceMock, converterMock);
    }

    @Test
    void givenNonExistentMovieWhenUpdateMovieExpectEntityNotFoundException() {
        // given
        MovieId movieId = MovieId.of(99L);
        MovieCreateRequest request = new MovieCreateRequest("Updated Movie", 2026);

        when(movieServiceMock
            .getMovie(movieId))
            .thenThrow(new EntityNotFoundException("Movie not found"));

        // when/then
        assertThrows(EntityNotFoundException.class, () -> moviePort.updateMovie(movieId, request));
        verify(movieServiceMock).getMovie(movieId);
        verifyNoMoreInteractions(movieServiceMock);
        verifyNoInteractions(converterMock);
    }

    @Test
    void givenExistingMovieWhenDeleteMovieExpectSuccessfulDeletion() {
        // given
        MovieId movieId = MovieId.of(1L);
        Movie movie = mock(Movie.class);

        when(movieServiceMock
            .getMovie(movieId))
            .thenReturn(movie);

        // when
        moviePort.deleteMovie(movieId);

        // then
        verify(movieServiceMock).getMovie(movieId);
        verify(movieServiceMock).delete(movie);
        verifyNoMoreInteractions(movieServiceMock);
        verifyNoInteractions(converterMock);
    }

    @Test
    void givenNonExistentMovieWhenDeleteMovieExpectEntityNotFoundException() {
        // given
        MovieId movieId = MovieId.of(99L);

        when(movieServiceMock
            .getMovie(movieId))
            .thenThrow(new EntityNotFoundException("Movie not found"));

        // when/then
        assertThrows(EntityNotFoundException.class, () -> moviePort.deleteMovie(movieId));
        verify(movieServiceMock).getMovie(movieId);
        verifyNoMoreInteractions(movieServiceMock);
        verifyNoInteractions(converterMock);
    }
}