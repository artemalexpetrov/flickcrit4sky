package com.flickcrit.app.infrastructure.persistence.repository;

import com.flickcrit.app.domain.model.common.EntityId;
import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.domain.model.rating.AverageRating;
import com.flickcrit.app.domain.model.rating.Rating;
import com.flickcrit.app.domain.model.user.UserId;
import com.flickcrit.app.domain.repository.RatingRepository;
import com.flickcrit.app.infrastructure.persistence.model.RatingEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
class RatingRepositoryImpl implements RatingRepository {

    private final JpaRatingRepository jpaRepository;
    private final ConversionService converter;

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

    private Rating convertToDomain(RatingEntity entity) {
        return converter.convert(entity, Rating.class);
    }

    private RatingEntity convertToEntity(Rating rate) {
        return converter.convert(rate, RatingEntity.class);
    }
}
