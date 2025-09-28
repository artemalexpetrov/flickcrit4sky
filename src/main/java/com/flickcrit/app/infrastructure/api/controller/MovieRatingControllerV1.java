package com.flickcrit.app.infrastructure.api.controller;

import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.domain.model.user.Email;
import com.flickcrit.app.domain.model.user.User;
import com.flickcrit.app.domain.service.UserService;
import com.flickcrit.app.infrastructure.api.model.rating.AverageRatingDto;
import com.flickcrit.app.infrastructure.api.model.rating.RateMovieRequest;
import com.flickcrit.app.infrastructure.api.model.rating.UserMovieRatingDto;
import com.flickcrit.app.infrastructure.api.port.RatingPort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/movies/{movieId:\\d+}/rating")
public class MovieRatingControllerV1 {

    private final RatingPort ratingPort;
    private final UserService userService;

    @GetMapping
    AverageRatingDto getRating(@PathVariable MovieId movieId) {
        return ratingPort.getMovieRating(movieId);
    }

    @PostMapping
    UserMovieRatingDto rateMovie(
        @AuthenticationPrincipal String username,
        @PathVariable MovieId movieId,
        @Valid @RequestBody RateMovieRequest request) {

        User user = loadUser(username);
        return ratingPort.rateMovie(user.getId(), movieId, request);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteRating(@AuthenticationPrincipal String username, @PathVariable MovieId movieId) {
        User user = loadUser(username);
        ratingPort.deleteRating(user.getId(), movieId);
    }

    private User loadUser(String username) {
        return userService.getUser(Email.of(username));
    }
}
