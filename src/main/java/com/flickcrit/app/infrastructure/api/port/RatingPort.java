package com.flickcrit.app.infrastructure.api.port;

import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.domain.model.user.UserId;
import com.flickcrit.app.infrastructure.api.model.rating.AverageRatingDto;
import com.flickcrit.app.infrastructure.api.model.rating.RateMovieRequest;
import com.flickcrit.app.infrastructure.api.model.rating.UserMovieRatingDto;

/**
 * Represents the port responsible for managing user ratings for movies.
 */
public interface RatingPort {

    /**
     * Retrieves the average rating for a given movie, including the rating value and the count of ratings.
     *
     * @param movieId the unique identifier of the movie for which the average rating is to be fetched
     * @return an AverageRatingDto containing the average rating value and the total number of ratings for the specified movie
     */
    AverageRatingDto getMovieRating(MovieId movieId);

    /**
     * Rates a movie on behalf of a user. The user's identity and rating details are provided
     * as parameters. This method processes the rating request and returns the updated
     * rating details for the movie.
     *
     * @param userId the unique identifier of the user submitting the rating
     * @param movieId the unique identifier of the movie to rate
     * @param rateRequest an object containing the movie ID and the rating value
     * @return the details of the user's rating for the specified movie, encapsulated in a UserMovieRatingDto
     */
    UserMovieRatingDto rateMovie(UserId userId, MovieId movieId, RateMovieRequest rateRequest);

    /**
     * Deletes an existing rating for a specified movie created by a specific user.
     * This method is used to remove the association of a user's rating with a movie.
     *
     * @param userId the unique identifier of the user whose rating is to be deleted
     * @param movieId the unique identifier of the movie for which the user's rating is to be deleted
     */
    void deleteRating(UserId userId, MovieId movieId);
}
