package com.flickcrit.app.domain.model.rating;

import com.flickcrit.app.domain.model.movie.MovieId;
import lombok.NonNull;

/**
 * Represents a movie along with its associated average rating.
 * This record combines a unique movie identifier and its respective
 * aggregated average rating information.
 *
 * The movie identifier should not be null and must represent a valid movie entity.
 * The average rating must not be null and encapsulates the calculated average
 * rating value and the total count of associated ratings.
 *
 * @param movieId the unique identifier of the movie
 * @param rating the average rating for the movie, including its calculated value
 *               and the number of ratings contributing to it
 */
public record RatedMovieId(@NonNull MovieId movieId, @NonNull AverageRating rating) {
}
