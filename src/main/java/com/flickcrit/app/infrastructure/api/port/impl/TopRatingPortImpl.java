package com.flickcrit.app.infrastructure.api.port.impl;

import com.flickcrit.app.domain.model.movie.Movie;
import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.domain.model.rating.RatedMovieId;
import com.flickcrit.app.domain.repository.MovieRepository;
import com.flickcrit.app.domain.repository.RatingRepository;
import com.flickcrit.app.infrastructure.api.model.movie.MovieDto;
import com.flickcrit.app.infrastructure.api.model.rating.RatedMovieDto;
import com.flickcrit.app.infrastructure.api.port.TopRatingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopRatingPortImpl implements TopRatingPort {

    private static final int TOP_RATED_MOVIES_LIMIT = 10;

    private final RatingRepository ratingRepository;
    private final MovieRepository movieRepository;
    private final ConversionService converter;

    @Override
    @Transactional
    public List<RatedMovieDto> getTopRatedMovies() {
        List<RatedMovieId> moviesIds = getTopRatedMovieIds();
        if (moviesIds.isEmpty()) {
            return List.of();
        }

        Map<MovieId, Movie> moviesById = getMoviesById(moviesIds);
        return zipWithRating(moviesIds, moviesById);
    }

    private List<RatedMovieDto> zipWithRating(List<RatedMovieId> ratedIds, Map<MovieId, Movie> moviesById) {
        return ratedIds.stream()
            .map(ratedId -> {
                Movie movie = moviesById.get(ratedId.movieId());
                MovieDto movieDto = convertToDto(movie);
                return new RatedMovieDto(movieDto, ratedId.rating());
            }).toList();
    }

    private MovieDto convertToDto(Movie movie) {
        return converter.convert(movie, MovieDto.class);
    }

    private Map<MovieId, Movie> getMoviesById(List<RatedMovieId> ratedMoviesIds) {
        List<MovieId> moviesIds = ratedMoviesIds.stream()
            .map(RatedMovieId::movieId)
            .toList();

        return movieRepository
            .findByIds(moviesIds).stream()
            .collect(Collectors.toMap(
                Movie::getId,
                Function.identity()
            ));
    }

    private List<RatedMovieId> getTopRatedMovieIds() {
        return ratingRepository.getTopRatedMovies(TOP_RATED_MOVIES_LIMIT);
    }
}
