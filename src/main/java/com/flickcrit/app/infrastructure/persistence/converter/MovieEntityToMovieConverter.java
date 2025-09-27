package com.flickcrit.app.infrastructure.persistence.converter;

import com.flickcrit.app.domain.model.movie.Isan;
import com.flickcrit.app.domain.model.movie.Movie;
import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.infrastructure.persistence.model.MovieEntity;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
class MovieEntityToMovieConverter implements Converter<MovieEntity, Movie> {

    @Override
    public Movie convert(@Nonnull MovieEntity entity) {
        return Movie.builder()
            .id(MovieId.optional(entity.getId()))
            .isan(Isan.of(entity.getIsan()))
            .title(entity.getTitle())
            .year(entity.getYear())
            .build();
    }
}
