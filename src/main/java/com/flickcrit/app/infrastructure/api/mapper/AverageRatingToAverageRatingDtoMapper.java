package com.flickcrit.app.infrastructure.api.mapper;

import com.flickcrit.app.domain.model.rating.AverageRating;
import com.flickcrit.app.infrastructure.api.model.rating.AverageRatingDto;
import com.flickcrit.app.infrastructure.core.conversion.config.MapStructConfig;
import jakarta.annotation.Nonnull;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapStructConfig.class)
interface AverageRatingToAverageRatingDtoMapper extends Converter<AverageRating, AverageRatingDto> {

    @Override
    AverageRatingDto convert(@Nonnull AverageRating rating);
}
