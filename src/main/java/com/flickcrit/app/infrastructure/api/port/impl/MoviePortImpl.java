package com.flickcrit.app.infrastructure.api.port.impl;

import com.flickcrit.app.domain.model.movie.Movie;
import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.domain.service.MovieService;
import com.flickcrit.app.infrastructure.api.model.common.PageRequestDto;
import com.flickcrit.app.infrastructure.api.model.common.PageResponse;
import com.flickcrit.app.infrastructure.api.model.movie.MovieCreateRequest;
import com.flickcrit.app.infrastructure.api.model.movie.MovieDto;
import com.flickcrit.app.infrastructure.api.port.MoviePort;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class MoviePortImpl implements MoviePort {

    private final MovieService service;
    private final ConversionService converter;

    @Override
    public PageResponse<MovieDto> getMovies(@NonNull PageRequestDto pageRequest) {
        Page<MovieDto> moviesPage = service
            .getMovies(PageRequest.of(pageRequest.getPage(), pageRequest.getSize()))
            .map(this::convertToDto);

        return PageResponse.of(moviesPage);
    }

    @Override
    public MovieDto getMovie(@NonNull MovieId id) {
        Movie movie = service.getMovie(id);
        return convertToDto(movie);
    }

    @Override
    public MovieDto createMovie(MovieCreateRequest request) {
        Movie movie = new Movie(request.title(), request.getYearAsObject());
        Movie savedMovie = service.save(movie);

        return convertToDto(savedMovie);
    }

    @Override
    public MovieDto updateMovie(MovieId id, MovieCreateRequest request) {
        Movie movie = service.getMovie(id);
        movie.setTitle(request.title())
            .setYear(request.getYearAsObject());

        Movie savedMovie = service.save(movie);
        return convertToDto(savedMovie);
    }

    @Override
    public void deleteMovie(MovieId id) {
        Movie movie = service.getMovie(id);
        service.delete(movie);
    }

    private MovieDto convertToDto(Movie movie) {
        return converter.convert(movie, MovieDto.class);
    }
}
