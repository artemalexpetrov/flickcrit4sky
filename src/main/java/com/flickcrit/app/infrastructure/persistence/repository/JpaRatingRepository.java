package com.flickcrit.app.infrastructure.persistence.repository;

import com.flickcrit.app.domain.model.rating.AverageRating;
import com.flickcrit.app.infrastructure.persistence.model.RatedMovieProjection;
import com.flickcrit.app.infrastructure.persistence.model.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface JpaRatingRepository extends JpaRepository<RatingEntity, Long> {

    /**
     * Retrieves a RatingEntity based on the provided user ID and movie ID.
     *
     * @param userId the unique identifier of the user; must not be null.
     * @param movieId the unique identifier of the movie; must not be null.
     * @return an Optional containing the matching RatingEntity if found, or an empty Optional otherwise.
     */
    Optional<RatingEntity> findByUserIdAndMovieId(Long userId, Long movieId);

    /**
     * Calculates the average rating and the total number of ratings for a given movie.
     *
     * @param movieId the unique identifier of the movie for which average rating is calculated; must not be null.
     * @return an Optional containing an AverageRating instance with the calculated average and count,
     *         or an empty Optional if no ratings exist for the specified movie.
     */
    @Query("""
        SELECT AVG(rating.rating) AS value, count(rating) as ratesCount
        FROM RatingEntity rating WHERE rating.movieId = :movieId
        GROUP BY rating.movieId""")
    Optional<AverageRating> getAverageRating(long movieId);

    /**
     * Retrieves a list of top-rated movies based on the average rating and number of ratings.
     *
     * @param topN the maximum number of top-rated movies to retrieve; must be a positive integer.
     * @return a list of RatedMovieProjection objects, each containing the movie ID, average rating,
     *         and count of ratings for the respective movie.
     */
    @Query(value = """
        SELECT rating.movie_id AS movieId, AVG(rating.rating) AS rating, count(rating) as ratesCount
        FROM rating rating
        GROUP BY rating.movie_id
        ORDER BY rating DESC
        LIMIT :topN""", nativeQuery = true)
    List<RatedMovieProjection> getTopRatedMovies(int topN);

}
