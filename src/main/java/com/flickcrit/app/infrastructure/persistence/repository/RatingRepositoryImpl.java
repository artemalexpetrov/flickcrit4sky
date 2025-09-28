package com.flickcrit.app.infrastructure.persistence.repository;

import com.flickcrit.app.domain.model.common.EntityId;
import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.domain.model.rating.AverageRating;
import com.flickcrit.app.domain.model.rating.RatedMovieId;
import com.flickcrit.app.domain.model.rating.Rating;
import com.flickcrit.app.domain.model.user.UserId;
import com.flickcrit.app.domain.repository.RatingRepository;
import com.flickcrit.app.infrastructure.persistence.model.RatedMovieProjection;
import com.flickcrit.app.infrastructure.persistence.model.RatingEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class RatingRepositoryImpl implements RatingRepository {

    private static final int MAX_TOP_RATED_MOVIES_LIMIT = 100;

    private final JpaRatingRepository jpaRepository;
    private final ConversionService converter;

    @Override
    public List<RatedMovieId> getTopRatedMovies(int n) {
        if (n <= 0 || n > MAX_TOP_RATED_MOVIES_LIMIT) {
           throw new IllegalArgumentException("Limit must be between 1 and " + MAX_TOP_RATED_MOVIES_LIMIT);
        }

        List<RatedMovieProjection> projections = jpaRepository.getTopRatedMovies(n);
        return projections.stream()
            .map(this::convertMovieRatingProjection)
            .toList();
    }

    @Override
    public Optional<AverageRating> getMovieRating(MovieId movieId) {
        return jpaRepository.getAverageRating(movieId.value());
    }

    @Override
    public Optional<Rating> findRating(@NonNull UserId userId, @NonNull MovieId movieId) {
        return jpaRepository
            .findByUserIdAndMovieId(userId.value(), movieId.value())
            .map(this::convertToDomain);
    }

    @Override
    public Rating save(@NonNull Rating rating) {
        RatingEntity ratingEntity = convertToEntity(rating);
        RatingEntity savedEntity = jpaRepository.save(ratingEntity);
        return convertToDomain(savedEntity);
    }

    @Override
    public void delete(@NonNull Rating rating) {
        Optional.ofNullable(rating.getId())
            .map(EntityId::value)
            .ifPresent(jpaRepository::deleteById);
    }

    private RatedMovieId convertMovieRatingProjection(RatedMovieProjection projection) {
        return converter.convert(projection, RatedMovieId.class);
    }

    private Rating convertToDomain(RatingEntity entity) {
        return converter.convert(entity, Rating.class);
    }

    private RatingEntity convertToEntity(Rating rate) {
        return converter.convert(rate, RatingEntity.class);
    }
}
