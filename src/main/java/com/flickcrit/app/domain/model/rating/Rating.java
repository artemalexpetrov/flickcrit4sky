package com.flickcrit.app.domain.model.rating;

import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.domain.model.user.UserId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

import static com.flickcrit.app.utils.ObjectUtils.requireNonNull;

/**
 * Represents a user's rating for a specific movie.
 */
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Rating {

    private final RatingId id;

    @EqualsAndHashCode.Include
    private final UserId userId;

    @EqualsAndHashCode.Include
    private final MovieId movieId;
    private final LocalDateTime createdAt;

    @EqualsAndHashCode.Include
    private RatingValue value;
    private LocalDateTime updatedAt;

    public Rating(UserId userId, MovieId movieId, RatingValue value) {
        this(null, userId, movieId, value, LocalDateTime.now(), LocalDateTime.now());
    }

    @Builder
    protected Rating(
        RatingId id, UserId userId, MovieId movieId, RatingValue value,
        LocalDateTime createdAt, LocalDateTime updatedAt) {

        this.id = id;
        this.userId = requireNonNull(userId, "User ID must be defined for rating");
        this.movieId = requireNonNull(movieId, "Movie ID must be defined for rating");
        this.value = requireNonNull(value, "Rating value must be defined");
        this.createdAt = requireNonNull(createdAt, "User rating time must be defined");
        this.updatedAt = requireNonNull(updatedAt, "User rating update time must be defined");
    }

    /**
     * Updates the user's rating value with the provided one.
     * Also updates the last modified timestamp to the current time.
     *
     * @param value the new value to assign; must not be null
     * @throws IllegalArgumentException if the provided value is null
     */
    public void updateRating(RatingValue value) {
        this.value = requireNonNull(value, "Rating value must be defined");
        this.updatedAt = LocalDateTime.now();
    }
}
