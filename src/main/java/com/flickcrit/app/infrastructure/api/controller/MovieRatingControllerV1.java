package com.flickcrit.app.infrastructure.api.controller;

import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.domain.model.user.Email;
import com.flickcrit.app.domain.model.user.User;
import com.flickcrit.app.domain.service.UserService;
import com.flickcrit.app.infrastructure.api.model.rating.AverageRatingDto;
import com.flickcrit.app.infrastructure.api.model.rating.RateMovieRequest;
import com.flickcrit.app.infrastructure.api.model.rating.UserMovieRatingDto;
import com.flickcrit.app.infrastructure.api.port.RatingPort;
import com.flickcrit.app.infrastructure.cache.config.CacheRegions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/movies/{movieId:\\d+}/rating")
@Tag(name = "Movie Ratings", description = "Endpoints for managing movie ratings")
public class MovieRatingControllerV1 {

    private final RatingPort ratingPort;
    private final UserService userService;

    @GetMapping
    @Operation(
        summary = "Get movie rating",
        description = "Retrieves the average rating for a movie")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved movie rating")
    @ApiResponse(responseCode = "404", description = "Movie not found")
    @Cacheable(value = CacheRegions.MOVIES_RATINGS, key = "#movieId", unless = "#result == null")
    public AverageRatingDto getRating(@Parameter(description = "ID of the movie") @PathVariable MovieId movieId) {
        return ratingPort.getMovieRating(movieId);
    }

    @PostMapping
    @Operation(summary = "Rate a movie", description = "Submit a rating for a movie")
    @ApiResponse(responseCode = "200", description = "Successfully rated the movie")
    @ApiResponse(responseCode = "404", description = "Movie not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @SecurityRequirement(name = "Bearer Authentication")
    public UserMovieRatingDto rateMovie(
        @Parameter(hidden = true) @AuthenticationPrincipal String username,
        @Parameter(description = "ID of the movie") @PathVariable MovieId movieId,
        @Valid @RequestBody RateMovieRequest request) {

        User user = loadUser(username);
        return ratingPort.rateMovie(user.getId(), movieId, request);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete movie rating", description = "Remove user's rating for a movie")
    @ApiResponse(responseCode = "204", description = "Rating successfully deleted")
    @ApiResponse(responseCode = "404", description = "Movie not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @SecurityRequirement(name = "Bearer Authentication")
    public void deleteRating(
        @Parameter(hidden = true) @AuthenticationPrincipal String username,
        @Parameter(description = "ID of the movie") @PathVariable MovieId movieId) {
        User user = loadUser(username);
        ratingPort.deleteRating(user.getId(), movieId);
    }

    private User loadUser(String username) {
        return userService.getUser(Email.of(username));
    }
}
