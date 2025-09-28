package com.flickcrit.app.infrastructure.persistence.converter;

import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.domain.model.rating.AverageRating;
import com.flickcrit.app.domain.model.rating.RatedMovieId;
import com.flickcrit.app.infrastructure.persistence.model.RatedMovieProjection;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RatedMovieProjectionToRatedMovieIdConverter implements Converter<RatedMovieProjection, RatedMovieId> {

    @Override
    public RatedMovieId convert(@Nonnull RatedMovieProjection projection) {
        AverageRating rating = AverageRating.of(projection.rating(), projection.ratesCount());
        return new RatedMovieId(MovieId.of(projection.movieId()), rating);
    }
}
