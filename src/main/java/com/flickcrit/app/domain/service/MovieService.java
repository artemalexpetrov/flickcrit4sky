package com.flickcrit.app.domain.service;

import com.flickcrit.app.domain.exception.EntityNotFoundException;
import com.flickcrit.app.domain.model.movie.Movie;
import com.flickcrit.app.domain.model.movie.MovieId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing movies.
 * Provides methods to retrieve, save, update, or delete movie entities.
 */
public interface MovieService {

    /**
     * Retrieves a movie by its unique identifier.
     *
     * @param id the unique identifier of the movie to retrieve
     * @return the movie associated with the given identifier
     * @throws EntityNotFoundException if the movie with the specified ID is not found
     */
    Movie getMovie(MovieId id);

    /**
     * Retrieves a paginated list of movies based on the given page request.
     *
     * @param pageRequest the page request containing pagination and sorting details
     * @return a page containing a list of movies
     */
    Page<Movie> getMovies(Pageable pageRequest);

    /**
     * Saves the provided movie entity, handling either creation or update.
     *
     * @param movie the movie entity to save, must not be null
     * @return the saved movie entity with potentially updated information
     */
    Movie save(Movie movie);

    /**
     * Deletes the specified movie from the system.
     *
     * @param movie the movie to delete, must not be null
     */
    void delete(Movie movie);
}
