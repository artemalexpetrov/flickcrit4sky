package com.flickcrit.app.domain.service.impl;

import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.domain.model.rating.AverageRating;
import com.flickcrit.app.domain.model.rating.Rating;
import com.flickcrit.app.domain.model.rating.RatingValue;
import com.flickcrit.app.domain.model.user.UserId;
import com.flickcrit.app.domain.repository.RatingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceImplTest {

    @Mock
    private RatingRepository repositoryMock;

    @InjectMocks
    private RatingServiceImpl ratingService;

    @Test
    void givenAverageMovieRatingWhenGetMovieRatingExpectAverageMovieRating() {
        // given
        MovieId movieId = MovieId.of(15L);
        AverageRating expectedRating = AverageRating.of(BigDecimal.valueOf(4), 5);
        when(repositoryMock
            .getMovieRating(any()))
            .thenReturn(Optional.of(expectedRating));

        // when
        AverageRating movieRating = ratingService.getMovieRating(movieId);

        // then
        assertEquals(expectedRating, movieRating);
        verify(repositoryMock).getMovieRating(movieId);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void givenNoRatingForMovieWhenGetMovieRatingExpectEmptyOptional() {
        // given
        MovieId movieId = MovieId.of(15L);

        when(repositoryMock
            .getMovieRating(any()))
            .thenReturn(Optional.empty());

        // when
        AverageRating movieRating = ratingService.getMovieRating(movieId);

        // then
        assertEquals(AverageRating.zero(), movieRating);
        verify(repositoryMock).getMovieRating(movieId);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void givenRatingWhenFindRatingExpectRating() {
        // given
        UserId userId = UserId.of(10L);
        MovieId movieId = MovieId.of(20L);
        Rating expectedRating = mock(Rating.class);

        when(repositoryMock
            .findRating(any(), any()))
            .thenReturn(Optional.of(expectedRating));

        // when
        Optional<Rating> result = ratingService.findRating(userId, movieId);

        // then
        assertTrue(result.isPresent());
        assertEquals(expectedRating, result.get());
        verify(repositoryMock).findRating(userId, movieId);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void givenNoRatingWhenFindRatingExpectEmptyOptional() {
        // given
        UserId userId = UserId.of(10L);
        MovieId movieId = MovieId.of(20L);

        when(repositoryMock
            .findRating(any(), any()))
            .thenReturn(Optional.empty());

        // when
        Optional<Rating> result = ratingService.findRating(userId, movieId);

        // then
        assertTrue(result.isEmpty());
        verify(repositoryMock).findRating(userId, movieId);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void givenNewRatingWhenSaveRatingExpectSavedRating() {
        // given
        Rating rating = new Rating(UserId.of(1L), MovieId.of(2L), RatingValue.of(1));
        Rating expectedRating = mock(Rating.class);

        when(repositoryMock
            .findRating(any(), any()))
            .thenReturn(Optional.empty());

        when(repositoryMock
            .save(any(Rating.class)))
            .thenReturn(expectedRating);

        // when
        Rating savedRating = ratingService.saveRating(rating);

        // then
        assertEquals(expectedRating, savedRating);
        verify(repositoryMock).findRating(rating.getUserId(), rating.getMovieId());
        verify(repositoryMock).save(rating);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void givenExistingRatingWhenSaveRatingExpectRatingUpdated() {
        // given
        Rating existingRating = new Rating(UserId.of(1L), MovieId.of(2L), RatingValue.of(1));
        Rating ratingToSave = new Rating(UserId.of(1L), MovieId.of(2L), RatingValue.of(4));
        Rating expectedRating = mock(Rating.class);

        when(repositoryMock
            .findRating(any(), any()))
            .thenReturn(Optional.of(existingRating));

        when(repositoryMock
            .save(any(Rating.class)))
            .thenReturn(expectedRating);

        // when
        Rating savedRating = ratingService.saveRating(ratingToSave);

        // then
        assertEquals(expectedRating, savedRating);
        verify(repositoryMock).findRating(ratingToSave.getUserId(), ratingToSave.getMovieId());
        verify(repositoryMock).save(assertArg(r -> assertEquals(ratingToSave.getValue(), r.getValue())));
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void givenNullRatingWhenSaveRatingExpectException() {
        // when/then
        assertThrows(IllegalArgumentException.class, () -> ratingService.saveRating(null));
        verifyNoInteractions(repositoryMock);
    }

    @Test
    void givenValidRatingWhenDeleteRatingExpectSuccess() {
        // given
        Rating rating = mock(Rating.class);

        // when
        ratingService.deleteRating(rating);

        // then
        verify(repositoryMock).delete(rating);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void givenNullRatingWhenDeleteRatingExpectException() {
        // when/then
        assertThrows(IllegalArgumentException.class, () -> ratingService.deleteRating(null));
        verifyNoInteractions(repositoryMock);
    }
}