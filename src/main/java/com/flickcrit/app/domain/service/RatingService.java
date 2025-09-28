package com.flickcrit.app.domain.service;

import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.domain.model.rating.AverageRating;
import com.flickcrit.app.domain.model.rating.Rating;
import com.flickcrit.app.domain.model.user.UserId;

import java.util.Optional;

public interface RatingService {

    AverageRating getMovieRating(MovieId movieId);

    /**
     * Finds the rating given by a specific user for a specific movie.
     *
     * @param userId the unique identifier of the user whose rating is being searched, must not be null
     * @param movieId the unique identifier of the movie for which the rating is being searched, must not be null
     * @return an Optional containing the found rating if it exists, or an empty Optional if no rating is found
     */
    Optional<Rating> findRating(UserId userId, MovieId movieId);

    /**
     * Saves the given rating entity to the system. If the rating already exists, updates the existing record.
     *
     * @param rating the rating entity to save, must not be null
     * @return the saved rating entity with potentially updated information
     */
    Rating saveRating(Rating rating);

    /**
     * Deletes the specified rating from the system.
     *
     * @param rating the rating entity to delete, must not be null
     */
    void deleteRating(Rating rating);
}
