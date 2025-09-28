package com.flickcrit.app.infrastructure.api.model.rating;

import com.flickcrit.app.domain.model.rating.AverageRating;
import com.flickcrit.app.infrastructure.api.model.movie.MovieDto;
import jakarta.annotation.Nonnull;

public record RatedMovieDto(

    @Nonnull
    MovieDto movie,

    @Nonnull
    AverageRating rating) {
}
