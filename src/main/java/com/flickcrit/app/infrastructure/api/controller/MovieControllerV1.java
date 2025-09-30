package com.flickcrit.app.infrastructure.api.controller;

import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.infrastructure.api.model.common.PageRequestDto;
import com.flickcrit.app.infrastructure.api.model.common.PageResponse;
import com.flickcrit.app.infrastructure.api.model.movie.MovieCreateRequest;
import com.flickcrit.app.infrastructure.api.model.movie.MovieDto;
import com.flickcrit.app.infrastructure.api.model.rating.RatedMovieDto;
import com.flickcrit.app.infrastructure.api.port.MoviePort;
import com.flickcrit.app.infrastructure.api.port.TopRatingPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Objects.requireNonNullElse;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/movies")
@Tag(name = "Movies", description = "Movie management endpoints")
public class MovieControllerV1 {

    private final MoviePort port;
    private final TopRatingPort topRatedMoviesPort;


    @Operation(summary = "Get all movies", description = "Returns a paginated list of all movies")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved movies")
    @GetMapping
    PageResponse<MovieDto> getMovies(@Valid PageRequestDto pagingRequest) {
        return port.getMovies(requireNonNullElse(pagingRequest, PageRequestDto.defaultRequest()));
    }

    @Operation(summary = "Get top rated movies", description = "Returns a list of top rated movies")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved top rated movies")
    @GetMapping("/top")
    List<RatedMovieDto> getTopRatedMovies() {
        return topRatedMoviesPort.getTopRatedMovies();
    }

    @Operation(summary = "Get movie by ID", description = "Returns a movie by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the movie")
    @ApiResponse(responseCode = "404", description = "Movie not found")
    @GetMapping("/{id:\\d+}")
    MovieDto getMovie(@Parameter(description = "Movie ID") @PathVariable MovieId id) {
        return port.getMovie(id);
    }

    @Operation(
        summary = "Create new movie",
        description = "Creates a new movie entry"
    )
    @ApiResponse(responseCode = "201", description = "Movie successfully created")
    @ApiResponse(responseCode = "403", description = "Access denied")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    MovieDto createMovie(@Parameter(description = "Movie details") @NotNull @RequestBody @Valid MovieCreateRequest request) {
        return port.createMovie(request);
    }

    @Operation(
        summary = "Update existing movie",
        description = "Updates an existing movie by its ID"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "Movie successfully updated")
    @ApiResponse(responseCode = "404", description = "Movie not found")
    @ApiResponse(responseCode = "403", description = "Access denied")
    @Secured("ROLE_ADMIN")
    @PutMapping("/{id:\\d+}")
    MovieDto updateMovie(@Parameter(description = "Movie ID") @PathVariable MovieId id,
                         @Parameter(description = "Updated movie details") @NotNull @RequestBody @Valid MovieCreateRequest request) {
        return port.updateMovie(id, request);
    }

    @Operation(
        summary = "Delete movie",
        description = "Deletes an existing movie by its ID"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "204", description = "Movie successfully deleted")
    @ApiResponse(responseCode = "404", description = "Movie not found")
    @ApiResponse(responseCode = "403", description = "Access denied")
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id:\\d+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteMovie(@Parameter(description = "Movie ID") @PathVariable MovieId id) {
        port.deleteMovie(id);
    }
}
