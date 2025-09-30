package com.flickcrit.app.infrastructure.api.port;

import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.infrastructure.api.model.common.PageRequestDto;
import com.flickcrit.app.infrastructure.api.model.common.PageResponse;
import com.flickcrit.app.infrastructure.api.model.movie.MovieCreateRequest;
import com.flickcrit.app.infrastructure.api.model.movie.MovieDto;

/**
 * Provides API operations for managing and retrieving movie entities.
 */
public interface MoviePort {

    /**
     * Retrieves a paginated list of movies.
     *
     * @param pageRequest the pagination and sorting information
     * @return a page containing a list of MovieDto objects
     */
    PageResponse<MovieDto> getMovies(PageRequestDto pageRequest);

    /**
     * Retrieves a movie by its unique identifier.
     *
     * @param id the unique identifier of the movie to retrieve
     * @return the details of the movie as a MovieDto
     * @throws com.flickcrit.app.domain.exception.EntityNotFoundException if the movie with the specified ID is not found
     */
    MovieDto getMovie(MovieId id);

    /**
     * Creates a new movie based on the provided request object.
     *
     * @param request the data required to create a new movie
     * @return a DTO representing the created movie
     */
    MovieDto createMovie(MovieCreateRequest request);

    /**
     * Updates an existing movie with the provided details.
     *
     * @param id the unique identifier of the movie to update
     * @param request the data containing updated movie details
     * @return a MovieDto representing the updated movie
     * @throws com.flickcrit.app.domain.exception.EntityNotFoundException if the movie with the specified ID is not found
     */
    MovieDto updateMovie(MovieId id, MovieCreateRequest request);

    /**
     * Deletes a movie by its unique identifier.
     *
     * @param id the unique identifier of the movie to delete
     * @throws com.flickcrit.app.domain.exception.EntityNotFoundException if the movie with the specified ID is not found
     */
    void deleteMovie(MovieId id);
}
