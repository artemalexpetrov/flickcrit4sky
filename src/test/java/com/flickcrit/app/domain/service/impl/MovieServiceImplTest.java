package com.flickcrit.app.domain.service.impl;

import com.flickcrit.app.domain.exception.EntityNotFoundException;
import com.flickcrit.app.domain.model.movie.Movie;
import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.domain.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    @Mock
    private MovieRepository repositoryMock;

    @InjectMocks
    private MovieServiceImpl movieService;

    @Test
    void givenMovieWhenGetMovieByIdExpectMovie() {
        // given
        MovieId movieId = MovieId.of(1L);
        Movie expectedMovie = mock(Movie.class);

        when(repositoryMock
            .findById(movieId))
            .thenReturn(Optional.of(expectedMovie));

        // when
        Movie actualMovie = movieService.getMovie(movieId);

        // then
        assertEquals(expectedMovie, actualMovie);
        verify(repositoryMock).findById(movieId);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void givenNoMovieWhenGetMovieByIdExpectMovie() {
        // given
        MovieId movieId = MovieId.of(1L);
        when(repositoryMock
            .findById(movieId))
            .thenReturn(Optional.empty());

        // when / then
        assertThrows(EntityNotFoundException.class, () -> movieService.getMovie(movieId));
        verify(repositoryMock).findById(movieId);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void givenNullableMovieIdWhenGetMovieByIdExpectException() {
        assertThrows(IllegalArgumentException.class, () -> movieService.getMovie(null));
    }

    @Test
    void givenMoviesWhenGetMoviesWithPagingRequestExpectMoviesPage() {
        // given
        Pageable pageRequest = mock(Pageable.class);
        Page<Movie> expectedPage = mock(Page.class);

        when(repositoryMock
            .findAll(pageRequest))
            .thenReturn(expectedPage);

        // when
        Page<Movie> actualPage = movieService.getMovies(pageRequest);

        // then
        assertEquals(expectedPage, actualPage);
        verify(repositoryMock).findAll(pageRequest);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void givenNullablePagingRequestWhenGetMoviesExpectException() {
        assertThrows(IllegalArgumentException.class, () -> movieService.getMovies(null));
    }

    @Test
    void givenMovieWhenSaveExpectSavedMovie() {
        // given
        Movie movieToSave = mock(Movie.class);
        Movie expectedMovie = mock(Movie.class);

        when(repositoryMock
            .save(movieToSave))
            .thenReturn(expectedMovie);

        // when
        Movie actualMovie = movieService.save(movieToSave);

        // then
        assertEquals(expectedMovie, actualMovie);
        verify(repositoryMock).save(movieToSave);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void givenNullableMovieWhenSaveExpectException() {
        assertThrows(IllegalArgumentException.class, () -> movieService.save(null));
    }

    @Test
    void givenMovieWhenDeleteExpectMovieDeleted() {
        // given
        Movie movieToDelete = mock(Movie.class);

        // when
        movieService.delete(movieToDelete);

        // then
        verify(repositoryMock).delete(movieToDelete);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void givenNullableMovieWhenDeleteExpectException() {
        assertThrows(IllegalArgumentException.class, () -> movieService.delete(null));
    }
}