package com.flickcrit.app.infrastructure.persistence.repository;

import com.flickcrit.app.domain.model.movie.Movie;
import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.infrastructure.persistence.model.MovieEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieRepositoryImplTest {

    @Mock
    private JpaMovieRepository jpaRepositoryMock;

    @Mock
    private ConversionService converterMock;

    @InjectMocks
    private MovieRepositoryImpl movieRepository;

    @Test
    void findByIdsWhenMoviesExistReturnsMovies() {
        // given
        List<Long> rawIds = List.of(10L);
        List<MovieId> moviesIds = List.of(MovieId.of(10L));

        MovieEntity movieEntity = mock(MovieEntity.class);
        Movie expectedMovie = mock(Movie.class);

        when(jpaRepositoryMock
            .findAllById(anyCollection()))
            .thenReturn(List.of(movieEntity));

        when(converterMock
            .convert(any(), eq(Movie.class)))
            .thenReturn(expectedMovie);

        // when
        List<Movie> result = movieRepository.findByIds(moviesIds);

        // then
        assertThat(result).containsExactly(expectedMovie);
        verify(jpaRepositoryMock).findAllById(rawIds);
        verify(converterMock).convert(movieEntity, Movie.class);
        verifyNoMoreInteractions(jpaRepositoryMock, converterMock);
    }

    @Test
    void findByIdWhenMovieExistsReturnsMovie() {
        // given
        MovieId movieId = MovieId.of(1L);
        MovieEntity movieEntity = mock(MovieEntity.class);
        Movie expectedMovie = mock(Movie.class);

        when(jpaRepositoryMock
            .findById(movieId.value()))
            .thenReturn(Optional.of(movieEntity));

        when(converterMock
            .convert(eq(movieEntity), eq(Movie.class)))
            .thenReturn(expectedMovie);

        // when
        Optional<Movie> result = movieRepository.findById(movieId);

        // then
        assertTrue(result.isPresent());
        assertEquals(expectedMovie, result.get());
        verify(jpaRepositoryMock).findById(movieId.value());
        verify(converterMock).convert(movieEntity, Movie.class);
        verifyNoMoreInteractions(jpaRepositoryMock, converterMock);
    }

    @Test
    void findByIdWhenMovieDoesNotExistReturnsEmptyOptional() {
        // given
        MovieId movieId = MovieId.of(1L);
        when(jpaRepositoryMock
            .findById(movieId.value()))
            .thenReturn(Optional.empty());

        // when
        Optional<Movie> result = movieRepository.findById(movieId);

        // then
        assertTrue(result.isEmpty());
        verify(jpaRepositoryMock).findById(movieId.value());
        verifyNoMoreInteractions(jpaRepositoryMock);
        verifyNoInteractions(converterMock);
    }

    @Test
    void findAllWhenMoviesExistReturnsPageWithMovies() {
        // given
        Pageable pageRequest = mock(Pageable.class);

        MovieEntity movieEntity = mock(MovieEntity.class);
        Movie expectedMovie = mock(Movie.class);

        Page<MovieEntity> movieEntitiesPage = new PageImpl<>(List.of(movieEntity));

        when(jpaRepositoryMock
            .findAll(pageRequest))
            .thenReturn(movieEntitiesPage);

        when(converterMock
            .convert(eq(movieEntity), eq(Movie.class)))
            .thenReturn(expectedMovie);

        // when
        Page<Movie> moviesPage = movieRepository.findAll(pageRequest);

        // then
        assertNotNull(moviesPage);
        assertThat(moviesPage).containsExactly(expectedMovie);
        verify(jpaRepositoryMock).findAll(pageRequest);
        verifyNoMoreInteractions(jpaRepositoryMock);
    }

    @Test
    void findAllWhenNoMoviesExistReturnsEmptyPage() {
        // given
        Pageable pageRequest = mock(Pageable.class);
        Page<MovieEntity> emptyPage = Page.empty();
        when(jpaRepositoryMock
            .findAll(pageRequest))
            .thenReturn(emptyPage);

        // when
        Page<Movie> result = movieRepository.findAll(pageRequest);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jpaRepositoryMock).findAll(pageRequest);
        verifyNoMoreInteractions(jpaRepositoryMock);
        verifyNoInteractions(converterMock);
    }

    @Test
    void saveWhenValidMovieThenReturnsSavedMovie() {
        // given
        Movie movieToSave = mock(Movie.class);
        MovieEntity entityToSave = mock(MovieEntity.class);
        MovieEntity savedEntity = mock(MovieEntity.class);
        Movie expectedMovie = mock(Movie.class);

        when(jpaRepositoryMock
            .save(entityToSave))
            .thenReturn(savedEntity);

        when(converterMock
            .convert(eq(movieToSave), eq(MovieEntity.class)))
            .thenReturn(entityToSave);

        when(converterMock
            .convert(eq(savedEntity), eq(Movie.class)))
            .thenReturn(expectedMovie);

        // when
        Movie result = movieRepository.save(movieToSave);

        // then
        assertNotNull(result);
        assertEquals(expectedMovie, result);
        verify(converterMock).convert(movieToSave, MovieEntity.class);
        verify(jpaRepositoryMock).save(entityToSave);
        verify(converterMock).convert(savedEntity, Movie.class);
        verifyNoMoreInteractions(jpaRepositoryMock, converterMock);
    }

    @Test
    void saveWhenNullMovieThenThrowsException() {
        // when/then
        assertThrows(IllegalArgumentException.class, () -> movieRepository.save(null));
        verifyNoInteractions(jpaRepositoryMock, converterMock);
    }

    @Test
    void deleteWhenValidMovieThenDeletesMovie() {
        // given
        Movie movieToDelete = mock(Movie.class);
        MovieId movieId = MovieId.of(15L);
        when(movieToDelete.getId()).thenReturn(movieId);

        // when
        movieRepository.delete(movieToDelete);

        // then
        verify(jpaRepositoryMock).deleteById(movieId.value());
        verifyNoMoreInteractions(jpaRepositoryMock);
        verifyNoInteractions(converterMock);
    }

    @Test
    void deleteWhenNullMovieThenThrowsException() {
        // when/then
        assertThrows(IllegalArgumentException.class, () -> movieRepository.delete(null));
        verifyNoInteractions(jpaRepositoryMock, converterMock);
    }
}