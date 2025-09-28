package com.flickcrit.app.infrastructure.persistence.converter;

import com.flickcrit.app.domain.model.rating.Rating;
import com.flickcrit.app.infrastructure.persistence.model.RatingEntity;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
class RatingToRatingEntityConverter implements Converter<Rating, RatingEntity> {

    @Override
    public RatingEntity convert(@Nonnull Rating rating) {
        Long ratingDbId = rating.getId() != null
            ? rating.getId().value()
            : null;

        return RatingEntity.builder()
            .id(ratingDbId)
            .userId(rating.getUserId().value())
            .movieId(rating.getMovieId().value())
            .rating(rating.getValue().value())
            .createdAt(rating.getCreatedAt())
            .updatedAt(rating.getUpdatedAt())
            .build();
    }
}
