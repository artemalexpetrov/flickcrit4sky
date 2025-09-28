package com.flickcrit.app.infrastructure.api.model.rating;

import com.flickcrit.app.domain.validation.ValidRating;
import org.springframework.validation.annotation.Validated;

/**
 * Represents a request to rate a movie.
 */
@Validated
public record RateMovieRequest(

    @ValidRating
    int rating
) {
}
