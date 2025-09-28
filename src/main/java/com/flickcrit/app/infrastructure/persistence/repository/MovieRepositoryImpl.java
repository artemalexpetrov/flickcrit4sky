package com.flickcrit.app.infrastructure.persistence.repository;

import com.flickcrit.app.domain.model.common.EntityId;
import com.flickcrit.app.domain.model.movie.Movie;
import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.domain.repository.MovieRepository;
import com.flickcrit.app.infrastructure.persistence.model.MovieEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class MovieRepositoryImpl implements MovieRepository {

    private final JpaMovieRepository jpaRepository;
    private final ConversionService converter;

    @Override
    public List<Movie> findByIds(Collection<MovieId> movieIds) {
        List<Long> rawMovieIds = movieIds.stream().map(MovieId::value).toList();
        return jpaRepository.findByIdIn(rawMovieIds).stream()
            .map(this::convertToDomain)
            .toList();
    }

    @Override
    public Optional<Movie> findById(@NonNull MovieId id) {
        return jpaRepository
            .findById(id.value())
            .map(this::convertToDomain);
    }

    @Override
    public Page<Movie> findAll(@NonNull Pageable pageRequest) {
        return jpaRepository
            .findAll(pageRequest)
            .map(this::convertToDomain);
    }

    @Override
    public Movie save(@NonNull Movie movie) {
        MovieEntity entityToSave = convertToEntity(movie);
        MovieEntity savedEntity = jpaRepository.save(entityToSave);

        return convertToDomain(savedEntity);
    }

    @Override
    public void delete(@NonNull Movie movie) {
        Optional.ofNullable(movie.getId())
            .map(EntityId::value)
            .ifPresent(jpaRepository::deleteById);
    }

    private Movie convertToDomain(MovieEntity entity) {
        return converter.convert(entity, Movie.class);
    }

    private MovieEntity convertToEntity(Movie movie) {
        return converter.convert(movie, MovieEntity.class);
    }
}
