package com.flickcrit.app.domain.repository;

import com.flickcrit.app.domain.model.movie.Movie;
import com.flickcrit.app.domain.model.movie.MovieId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Interface for managing Movie entities in a data repository.
 * Provides methods for CRUD operations and retrieval of movies.
 */
public interface MovieRepository {

    /**
     * Retrieves a list of movies identified by their unique identifiers.
     * If no matching movies are found for the provided identifiers,
     * the returned list will be empty.
     *
     * @param movieIds a collection of unique identifiers representing the movies to retrieve; must not be null
     * @return a list of Movie objects corresponding to the provided identifiers, or an empty list if no matches are found
     */
    List<Movie> findByIds(Collection<MovieId> movieIds);

    /**
     * Retrieves a movie by its unique identifier.
     *
     * @param id the unique identifier of the movie to find; must not be null
     * @return an {@code Optional} containing the found movie,
     * or an empty {@code Optional} if no movie exists with the given identifier
     */
    Optional<Movie> findById(MovieId id);

    /**
     * Retrieves a paginated list of all Movie entities.
     *
     * @param pageRequest the pagination and sorting information; must not be null
     * @return a Page containing the Movie entities for the specified pagination
     */
    Page<Movie> findAll(Pageable pageRequest);

    /**
     * Saves the provided Movie entity to the data repository.
     * If the movie already exists, it updates the existing entry;
     * otherwise, a new entry is created.
     *
     * @param movie the Movie entity to save; must not be null
     * @return the saved Movie entity
     */
    Movie save(Movie movie);

    /**
     * Deletes the specified movie from the repository.
     *
     * @param movie the movie entity to be deleted; must not be null
     */
    void delete(Movie movie);
}
