package com.flickcrit.app.infrastructure.api.port;

import com.flickcrit.app.infrastructure.api.model.rating.RatedMovieDto;

import java.util.List;

/**
 * Technically it's a USE CASE, but for the sake of simplicity
 * let's keep it that way
 */
public interface TopRatingPort {

    /**
     * Retrieves a list of top-rated movies, where each movie is represented by a DTO containing
     * its details and associated average rating information. The returned list is sorted in
     * descending order by the average rating of the movies.
     *
     * @return a list of RatedMovieDto objects, each containing details about the movie and its
     *         corresponding average rating
     */
    List<RatedMovieDto> getTopRatedMovies();
}
