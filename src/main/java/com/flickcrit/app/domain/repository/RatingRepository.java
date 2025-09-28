package com.flickcrit.app.domain.repository;

import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.domain.model.rating.AverageRating;
import com.flickcrit.app.domain.model.rating.Rating;
import com.flickcrit.app.domain.model.user.UserId;

import java.util.Optional;

/**
 * Interface for managing Rating entities in a data repository.
 */
public interface RatingRepository {

    /**
     * Retrieves the average rating for a specific movie based on user ratings.
     *
     * @param movieId the unique identifier of the movie whose average rating is to be retrieved, must not be null
     * @return an Optional containing the average rating if it exists, or an empty Optional if the movie has no ratings
     */
    Optional<AverageRating> getMovieRating(MovieId movieId);

    /**
     * Retrieves a user's rating for a specific movie.
     *
     * @param userId the unique identifier of the user; must not be null
     * @param movieId the unique identifier of the movie; must not be null
     * @return an {@code Optional} containing the found {@code Rating},
     * or an empty {@code Optional} if no matching rating exists
     */
    Optional<Rating> findRating(UserId userId, MovieId movieId);

    /**
     * Saves a given Rating entity to the data repository.
     * If the rate already exists, it updates the existing entry;
     * otherwise, a new entry is created.
     *
     * @param rate the Rating entity to save; must not be null
     * @return the saved Rating entity
     */
    Rating save(Rating rate);

    /**
     * Deletes the specified Rating entity from the repository.
     *
     * @param rate the Rating entity to be deleted; must not be null
     */
    void delete(Rating rate);
}
