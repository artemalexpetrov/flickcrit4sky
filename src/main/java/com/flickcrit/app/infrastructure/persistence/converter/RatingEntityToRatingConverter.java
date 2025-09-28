package com.flickcrit.app.infrastructure.persistence.converter;

import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.domain.model.rating.Rating;
import com.flickcrit.app.domain.model.rating.RatingId;
import com.flickcrit.app.domain.model.rating.RatingValue;
import com.flickcrit.app.domain.model.user.UserId;
import com.flickcrit.app.infrastructure.persistence.model.RatingEntity;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
class RatingEntityToRatingConverter implements Converter<RatingEntity, Rating> {

    @Override
    public Rating convert(@Nonnull RatingEntity entity) {
        return Rating.builder()
            .id(RatingId.optional(entity.getId()))
            .userId(UserId.of(entity.getUserId()))
            .movieId(MovieId.of(entity.getMovieId()))
            .value(RatingValue.of(entity.getRating()))
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
