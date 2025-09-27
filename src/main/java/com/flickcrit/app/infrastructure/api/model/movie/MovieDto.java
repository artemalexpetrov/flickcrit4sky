package com.flickcrit.app.infrastructure.api.model.movie;

import jakarta.annotation.Nonnull;
import lombok.Builder;

import java.util.UUID;

/**
 * A Data Transfer Object (DTO) representing a movie.
 */
@Builder
public record MovieDto(

    @Nonnull
    Long id,

    @Nonnull
    UUID isan,

    @Nonnull
    String title,

    int year
) {

}
