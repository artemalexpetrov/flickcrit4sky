package com.flickcrit.app.infrastructure.api.model.rating;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Data Transfer Object representing a user's rating for a movie.
 */
@Builder
public record UserMovieRatingDto(

    @Nonnull
    Long movieId,

    @Nullable
    Integer rating,

    @Nullable
    @JsonInclude(JsonInclude.Include.NON_NULL)
    LocalDateTime createdAt,

    @Nullable
    @JsonInclude(JsonInclude.Include.NON_NULL)
    LocalDateTime updatedAt
) {

    /**
     * Creates a UserMovieRatingDto instance with the provided movie ID and no rating.
     *
     * @param movieId the identifier of the movie for which the rating is to be created
     * @return a UserMovieRatingDto instance with the specified movie ID and no rating
     */
    public static UserMovieRatingDto nonRated(Long movieId) {
        return  UserMovieRatingDto.builder()
            .movieId(movieId)
            .build();
    }

}
