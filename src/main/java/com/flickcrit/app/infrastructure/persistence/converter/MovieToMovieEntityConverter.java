package com.flickcrit.app.infrastructure.persistence.converter;

import com.flickcrit.app.domain.model.common.EntityId;
import com.flickcrit.app.domain.model.movie.Movie;
import com.flickcrit.app.infrastructure.persistence.model.MovieEntity;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class MovieToMovieEntityConverter implements Converter<Movie, MovieEntity> {

    @Override
    public MovieEntity convert(@Nonnull Movie movie) {
        Long movieId = Optional.ofNullable(movie.getId())
            .map(EntityId::value)
            .orElse(null);

        return MovieEntity.builder()
            .id(movieId)
            .isan(movie.getIsan().value())
            .title(movie.getTitle())
            .year(movie.getYear())
            .build();
    }
}
