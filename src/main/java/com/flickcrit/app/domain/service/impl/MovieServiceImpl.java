package com.flickcrit.app.domain.service.impl;

import com.flickcrit.app.domain.exception.EntityNotFoundException;
import com.flickcrit.app.domain.model.movie.Movie;
import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.domain.repository.MovieRepository;
import com.flickcrit.app.domain.service.MovieService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class MovieServiceImpl implements MovieService {

    private final MovieRepository repository;

    @Override
    public Movie getMovie(@NonNull MovieId id) {
        return repository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Movie not found by ID " + id.value()));
    }

    @Override
    public Page<Movie> getMovies(@NonNull Pageable pageRequest) {
        return repository.findAll(pageRequest);
    }

    @Override
    @Transactional
    public Movie save(@NonNull Movie movie) {
        log.info("Saving a movie with ID {}", movie.getId());
        return repository.save(movie);
    }

    @Override
    @Transactional
    public void delete(@NonNull Movie movie) {
        log.info("Deleting a movie with ID {}", movie.getId());
        repository.delete(movie);
    }
}
