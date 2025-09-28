package com.flickcrit.app.infrastructure.api.mapper;

import com.flickcrit.app.domain.model.rating.Rating;
import com.flickcrit.app.infrastructure.api.model.rating.UserMovieRatingDto;
import com.flickcrit.app.infrastructure.core.conversion.config.MapStructConfig;
import jakarta.annotation.Nonnull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapStructConfig.class)
interface RatingToUserMovieRatingDtoMapper extends Converter<Rating, UserMovieRatingDto> {

    @Override
    @Mapping(target = "movieId", expression = "java(rating.getMovieId().value())")
    @Mapping(target = "rating", expression = "java(rating.getValue().value())")
    UserMovieRatingDto convert(@Nonnull Rating rating);

}
