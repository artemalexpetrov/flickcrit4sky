package com.flickcrit.app.infrastructure.api.port.impl;

import com.flickcrit.app.domain.model.movie.Movie;
import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.domain.model.rating.AverageRating;
import com.flickcrit.app.domain.model.rating.Rating;
import com.flickcrit.app.domain.model.user.UserId;
import com.flickcrit.app.domain.service.MovieService;
import com.flickcrit.app.domain.service.RatingService;
import com.flickcrit.app.infrastructure.api.model.rating.AverageRatingDto;
import com.flickcrit.app.infrastructure.api.model.rating.RateMovieRequest;
import com.flickcrit.app.infrastructure.api.model.rating.UserMovieRatingDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingPortImplTest {

    @Mock
    private MovieService movieServiceMock;

    @Mock
    private RatingService ratingServiceMock;

    @Mock
    private ConversionService converterMock;

    @InjectMocks
    private RatingPortImpl port;
    
    @Test
    void givenExistingMovieWhenGetMovieRatingExpectMovieRating() {
        // given
        MovieId movieId = MovieId.of(19L);
        Movie movie = mock(Movie.class);
        AverageRating rating = mock(AverageRating.class);
        AverageRatingDto expectedRatingDto = mock(AverageRatingDto.class);

        when(movie.getId()).thenReturn(movieId);
        when(movieServiceMock
            .getMovie(any()))
            .thenReturn(movie);

        when(ratingServiceMock
            .getMovieRating(any()))
            .thenReturn(rating);

        when(converterMock
            .convert(any(), eq(AverageRatingDto.class)))
            .thenReturn(expectedRatingDto);

        // when
        AverageRatingDto actualRating = port.getMovieRating(movieId);

        // then
        assertEquals(expectedRatingDto, actualRating);
        verify(movieServiceMock).getMovie(movieId);
        verify(ratingServiceMock).getMovieRating(movieId);
        verify(converterMock).convert(rating, AverageRatingDto.class);
        verifyNoMoreInteractions(movieServiceMock, ratingServiceMock, converterMock);
    }

    @Test
    void givenNullableMovieIdWhenGetMovieRatingExpectException() {
        assertThrows(IllegalArgumentException.class, () -> port.getMovieRating(null));
    }

    @Test
    void givenNonExistingMovieWhenGetMovieRatingExpectException() {
        // given
        MovieId movieId = MovieId.of(19L);
        when(movieServiceMock
            .getMovie(any()))
            .thenThrow(EntityNotFoundException.class);

        // when & then
        assertThrows(EntityNotFoundException.class, () -> port.getMovieRating(movieId));
        verify(movieServiceMock).getMovie(movieId);
        verifyNoInteractions(ratingServiceMock, converterMock);
    }

    @Test
    void givenRatingRequestWhenRateMovieExpectMovieRated() {
        // given
        MovieId movieId = MovieId.of(19L);
        UserId userId = UserId.of(11L);

        Movie movie = mock(Movie.class);
        Rating rating = mock(Rating.class);

        RateMovieRequest rateRequest = new RateMovieRequest(4);
        UserMovieRatingDto expectedRatingDto = mock(UserMovieRatingDto.class);

        when(movie.getId()).thenReturn(movieId);
        when(movieServiceMock
            .getMovie(any()))
            .thenReturn(movie);

        when(ratingServiceMock
            .saveRating(any()))
            .thenReturn(rating);

        when(converterMock
            .convert(rating, UserMovieRatingDto.class))
            .thenReturn(expectedRatingDto);

        // when
        UserMovieRatingDto actualRating = port.rateMovie(userId, movieId, rateRequest);

        // then
        assertEquals(expectedRatingDto, actualRating);
        verify(movieServiceMock).getMovie(movieId);
        verify(ratingServiceMock).saveRating(assertArg(ratingToSave -> {
            assertEquals(userId, ratingToSave.getUserId());
            assertEquals(movieId, ratingToSave.getMovieId());
            assertEquals(rateRequest.rating(), ratingToSave.getValue().value());
        }));
        verify(converterMock).convert(rating, UserMovieRatingDto.class);
        verifyNoMoreInteractions(movieServiceMock, ratingServiceMock, converterMock);
    }

    @Test
    void givenNonExistingMovieWhenRateMovieExpectException() {
        // given
        MovieId movieId = MovieId.of(19L);
        UserId userId = UserId.of(11L);
        RateMovieRequest rateRequest = new RateMovieRequest(4);

        when(movieServiceMock
            .getMovie(any()))
            .thenThrow(EntityNotFoundException.class);

        // when & then
        assertThrows(EntityNotFoundException.class, () -> port.rateMovie(userId, movieId, rateRequest));
        verify(movieServiceMock).getMovie(movieId);
        verifyNoInteractions(ratingServiceMock, converterMock);
    }
    
    @Test
    void givenNullUserIdWhenRateMovieExpectException() {
        MovieId movieId = MovieId.of(19L);
        RateMovieRequest rateRequest = new RateMovieRequest(4);

        assertThrows(IllegalArgumentException.class, () -> port.rateMovie(null, movieId, rateRequest));
        verifyNoInteractions(movieServiceMock, ratingServiceMock, converterMock);
    }

    @Test
    void givenNullMovieIdWhenRateMovieExpectException() {
        UserId userId = UserId.of(11L);
        RateMovieRequest rateRequest = new RateMovieRequest(4);

        assertThrows(IllegalArgumentException.class, () -> port.rateMovie(userId, null, rateRequest));
        verifyNoInteractions(movieServiceMock, ratingServiceMock, converterMock);
    }

    @Test
    void givenNullRateRequestWhenRateMovieExpectException() {
        UserId userId = UserId.of(11L);
        MovieId movieId = MovieId.of(19L);

        assertThrows(IllegalArgumentException.class, () -> port.rateMovie(userId, movieId, null));
        verifyNoInteractions(movieServiceMock, ratingServiceMock, converterMock);
    }

    @Test
    void givenInvalidRatingValueWhenRateMovieExpectException() {
        UserId userId = UserId.of(11L);
        MovieId movieId = MovieId.of(19L);
        RateMovieRequest rateRequest = new RateMovieRequest(11);

        assertThrows(IllegalArgumentException.class, () -> port.rateMovie(userId, movieId, rateRequest));
        verifyNoInteractions(movieServiceMock, ratingServiceMock, converterMock);
    }

    @Test
    void givenExistingRatingWhenDeleteRatingExpectRatingDeleted() {
        // given
        MovieId movieId = MovieId.of(19L);
        UserId userId = UserId.of(11L);
        Rating rating = mock(Rating.class);

        when(ratingServiceMock.findRating(userId, movieId))
            .thenReturn(java.util.Optional.of(rating));

        // when
        port.deleteRating(userId, movieId);

        // then
        verify(ratingServiceMock).findRating(userId, movieId);
        verify(ratingServiceMock).deleteRating(rating);
        verifyNoMoreInteractions(ratingServiceMock);
        verifyNoInteractions(movieServiceMock, converterMock);
    }

    @Test
    void givenNonExistingRatingWhenDeleteRatingExpectNoAction() {
        // given
        MovieId movieId = MovieId.of(19L);
        UserId userId = UserId.of(11L);

        when(ratingServiceMock.findRating(userId, movieId))
            .thenReturn(java.util.Optional.empty());

        // when
        port.deleteRating(userId, movieId);

        // then
        verify(ratingServiceMock).findRating(userId, movieId);
        verifyNoMoreInteractions(ratingServiceMock);
        verifyNoInteractions(movieServiceMock, converterMock);
    }

    @Test
    void givenNullUserIdWhenDeleteRatingExpectException() {
        MovieId movieId = MovieId.of(19L);

        assertThrows(IllegalArgumentException.class, () -> port.deleteRating(null, movieId));
        verifyNoInteractions(movieServiceMock, ratingServiceMock, converterMock);
    }

    @Test
    void givenNullMovieIdWhenDeleteRatingExpectException() {
        UserId userId = UserId.of(11L);

        assertThrows(IllegalArgumentException.class, () -> port.deleteRating(userId, null));
        verifyNoInteractions(movieServiceMock, ratingServiceMock, converterMock);
    }

}