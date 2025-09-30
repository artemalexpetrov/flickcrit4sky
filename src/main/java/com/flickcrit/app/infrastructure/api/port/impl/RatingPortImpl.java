package com.flickcrit.app.infrastructure.api.port.impl;

import com.flickcrit.app.domain.model.movie.Movie;
import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.domain.model.rating.AverageRating;
import com.flickcrit.app.domain.model.rating.Rating;
import com.flickcrit.app.domain.model.rating.RatingValue;
import com.flickcrit.app.domain.model.user.UserId;
import com.flickcrit.app.domain.service.MovieService;
import com.flickcrit.app.domain.service.RatingService;
import com.flickcrit.app.infrastructure.api.model.rating.AverageRatingDto;
import com.flickcrit.app.infrastructure.api.model.rating.RateMovieRequest;
import com.flickcrit.app.infrastructure.api.model.rating.UserMovieRatingDto;
import com.flickcrit.app.infrastructure.api.port.RatingPort;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class RatingPortImpl implements RatingPort {

    private final MovieService movieService;
    private final RatingService ratingService;
    private final ConversionService converter;

    @Override
    public AverageRatingDto getMovieRating(@NonNull MovieId movieId) {
        Movie movie = getMovieOrThrow(movieId);
        AverageRating movieRating = ratingService.getMovieRating(movie.getId());
        return convertToAverageRatingDto(movieRating);
    }

    @Override
    public UserMovieRatingDto rateMovie(@NonNull UserId userId, @NonNull MovieId movieId, @NonNull RateMovieRequest rateRequest) {
        RatingValue ratingValue = RatingValue.of(rateRequest.rating());
        Movie movie = getMovieOrThrow(movieId);
        Rating rating = new Rating(userId, movie.getId(), ratingValue);
        Rating updatedRating = ratingService.saveRating(rating);

        return convertToUserRateDto(updatedRating);
    }

    @Override
    public void deleteRating(@NonNull UserId userId, @NonNull MovieId movieId) {
        ratingService
            .findRating(userId, movieId)
            .ifPresent(ratingService::deleteRating);
    }

    private UserMovieRatingDto convertToUserRateDto(Rating rating) {
        return converter.convert(rating, UserMovieRatingDto.class);
    }

    private AverageRatingDto convertToAverageRatingDto(AverageRating rating) {
        return converter.convert(rating, AverageRatingDto.class);
    }

    private Movie getMovieOrThrow(MovieId movieId) {
        return movieService.getMovie(movieId);
    }
}
