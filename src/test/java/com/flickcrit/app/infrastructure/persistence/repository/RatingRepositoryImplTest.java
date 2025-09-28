package com.flickcrit.app.infrastructure.persistence.repository;

import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.domain.model.rating.AverageRating;
import com.flickcrit.app.domain.model.rating.RatedMovieId;
import com.flickcrit.app.domain.model.rating.Rating;
import com.flickcrit.app.domain.model.rating.RatingId;
import com.flickcrit.app.domain.model.user.UserId;
import com.flickcrit.app.infrastructure.persistence.model.RatedMovieProjection;
import com.flickcrit.app.infrastructure.persistence.model.RatingEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingRepositoryImplTest {

    @Mock
    private JpaRatingRepository jpaRepository;

    @Mock
    private ConversionService converterMock;

    @InjectMocks
    private RatingRepositoryImpl repository;

    @Test
    void givenTopRatedMoviesWhenGetTopRatedMoviesExpectConvertedList() {
        // given
        int limit = 10;
        RatedMovieProjection projection = mock(RatedMovieProjection.class);
        RatedMovieId expectedMovieId = new RatedMovieId(MovieId.of(10L), AverageRating.of(BigDecimal.ONE, 2));

        when(jpaRepository
            .getTopRatedMovies(anyInt()))
            .thenReturn(List.of(projection));

        when(converterMock
            .convert(any(), eq(RatedMovieId.class)))
            .thenReturn(expectedMovieId);

        // when
        List<RatedMovieId> result = repository.getTopRatedMovies(limit);

        // then
        assertThat(result).containsExactly(expectedMovieId);
        verify(jpaRepository).getTopRatedMovies(limit);
        verify(converterMock).convert(projection, RatedMovieId.class);
        verifyNoMoreInteractions(jpaRepository, converterMock);
    }

    @Test
    void givenNoTopRatedMoviesWhenGetTopRatedMoviesExpectEmptyList() {
        // given
        int limit = 10;
        when(jpaRepository
            .getTopRatedMovies(anyInt()))
            .thenReturn(List.of());

        // when
        List<RatedMovieId> result = repository.getTopRatedMovies(limit);

        // then
        assertThat(result).isEmpty();
        verify(jpaRepository).getTopRatedMovies(limit);
        verifyNoMoreInteractions(jpaRepository);
        verifyNoInteractions(converterMock);
    }

    @Test
    void givenInvalidLimitWhenGetTopRatedMoviesExpectException() {
        assertThrows(IllegalArgumentException.class, () -> repository.getTopRatedMovies(0));
        assertThrows(IllegalArgumentException.class, () -> repository.getTopRatedMovies(-1));
        verifyNoInteractions(jpaRepository, converterMock);
    }

    @Test
    void givenAverageMovieRatingWhenGetMovieRatingExpectAverageMovieRating() {
        // given
        MovieId movieId = MovieId.of(15L);
        AverageRating rating = AverageRating.zero();
        when(jpaRepository
            .getAverageRating(anyLong()))
            .thenReturn(Optional.of(rating));

        // when
        Optional<AverageRating> movieRating = repository.getMovieRating(movieId);

        // then
        assertThat(movieRating).contains(rating);
        verify(jpaRepository).getAverageRating(movieId.value());
        verifyNoMoreInteractions(jpaRepository);
    }

    @Test
    void givenNoRatingForMovieWhenGetMovieRatingExpectEmptyOptional() {
        // given
        MovieId movieId = MovieId.of(15L);

        when(jpaRepository
            .getAverageRating(anyLong()))
            .thenReturn(Optional.empty());

        // when
        Optional<AverageRating> movieRating = repository.getMovieRating(movieId);

        // then
        assertThat(movieRating).isEmpty();
        verify(jpaRepository).getAverageRating(movieId.value());
        verifyNoMoreInteractions(jpaRepository);
    }

    @Test
    void givenRatingWhenFindByUserAndMovieIdsExpectRating() {
        // given
        UserId userId = UserId.of(10L);
        MovieId movieId = MovieId.of(20L);

        RatingEntity ratingEntity = mock(RatingEntity.class);
        Rating expectedRating = mock(Rating.class);

        when(jpaRepository
            .findByUserIdAndMovieId(any(), any()))
            .thenReturn(Optional.of(ratingEntity));

        when(converterMock
            .convert(any(), eq(Rating.class)))
            .thenReturn(expectedRating);

        // when
        Optional<Rating> rating = repository.findRating(userId, movieId);

        // then
        assertThat(rating).contains(expectedRating);
        verify(jpaRepository).findByUserIdAndMovieId(userId.value(), movieId.value());
        verify(converterMock).convert(ratingEntity, Rating.class);
        verifyNoMoreInteractions(jpaRepository, converterMock);
    }

    @Test
    void givenNoRatingWheFindByUserAndMovieIdExpectEmptyOptional() {
        // given
        UserId userId = UserId.of(10L);
        MovieId movieId = MovieId.of(20L);

        when(jpaRepository
            .findByUserIdAndMovieId(any(), any()))
            .thenReturn(Optional.empty());

        // when
        Optional<Rating> rating = repository.findRating(userId, movieId);

        // then
        assertThat(rating).isEmpty();
        verify(jpaRepository).findByUserIdAndMovieId(userId.value(), movieId.value());
        verifyNoMoreInteractions(jpaRepository);
        verifyNoInteractions(converterMock);
    }

    @Test
    void givenNullableUserAndMovieIdWhenFindRatingExpectException() {
        // given
        UserId userId = UserId.of(10L);
        MovieId movieId = MovieId.of(20L);

        // when / then
        assertThrows(IllegalArgumentException.class, () -> repository.findRating(userId, null));
        assertThrows(IllegalArgumentException.class, () -> repository.findRating(null, movieId));
        verifyNoInteractions(jpaRepository, converterMock);
    }

    @Test
    void givenRatingWhenSaveExpectConvertedAndSavedRating() {
        // given
        Rating rating = mock(Rating.class);
        RatingEntity ratingEntity = mock(RatingEntity.class);
        RatingEntity savedEntity = mock(RatingEntity.class);
        Rating expectedRating = mock(Rating.class);

        when(converterMock
            .convert(any(), eq(RatingEntity.class)))
            .thenReturn(ratingEntity);

        when(jpaRepository
            .save(any()))
            .thenReturn(savedEntity);

        when(converterMock
            .convert(any(), eq(Rating.class)))
            .thenReturn(expectedRating);

        // when
        Rating result = repository.save(rating);

        // then
        assertThat(result).isEqualTo(expectedRating);
        verify(converterMock).convert(rating, RatingEntity.class);
        verify(jpaRepository).save(ratingEntity);
        verify(converterMock).convert(savedEntity, Rating.class);
        verifyNoMoreInteractions(jpaRepository, converterMock);
    }

    @Test
    void givenNullRatingWhenSaveExpectException() {
        assertThrows(IllegalArgumentException.class, () -> repository.save(null));
        verifyNoInteractions(jpaRepository, converterMock);
    }

    @Test
    void givenRatingWithIdWhenDeleteExpectDeleteById() {
        // given
        Rating rating = mock(Rating.class);
        RatingId ratingId = RatingId.of(10L);
        when(rating.getId()).thenReturn(ratingId);

        // when
        repository.delete(rating);

        // then
        verify(jpaRepository).deleteById(ratingId.value());
        verifyNoMoreInteractions(jpaRepository);
        verifyNoInteractions(converterMock);
    }

    @Test
    void givenNullRatingWhenDeleteExpectException() {
        assertThrows(IllegalArgumentException.class, () -> repository.delete(null));
        verifyNoInteractions(jpaRepository, converterMock);
    }
}