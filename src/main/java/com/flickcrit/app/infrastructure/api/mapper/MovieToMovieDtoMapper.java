package com.flickcrit.app.infrastructure.api.mapper;

import com.flickcrit.app.domain.model.movie.Movie;
import com.flickcrit.app.infrastructure.api.model.movie.MovieDto;
import com.flickcrit.app.infrastructure.core.conversion.config.MapStructConfig;
import jakarta.annotation.Nonnull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapStructConfig.class)
interface MovieToMovieDtoMapper extends Converter<Movie, MovieDto> {

    @Override
    @Mapping(target = "isan", expression = "java(movie.getIsan().value())")
    @Mapping(target = "year", expression = "java(movie.getYear().getValue())")
    MovieDto convert(@Nonnull Movie movie);

}
