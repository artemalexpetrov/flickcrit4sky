package com.flickcrit.app.infrastructure.api.controller;

import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.infrastructure.api.model.common.PageResponse;
import com.flickcrit.app.infrastructure.api.model.movie.MovieCreateRequest;
import com.flickcrit.app.infrastructure.api.model.movie.MovieDto;
import com.flickcrit.app.infrastructure.api.port.MoviePort;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/movies")
public class MovieControllerV1 {

    private final MoviePort port;

    @GetMapping
    PageResponse<MovieDto> getMovies(@Valid Pageable pagingRequest) {
        return port.getMovies(pagingRequest);
    }

    @GetMapping("/{id:\\d+}")
    MovieDto getMovie(@PathVariable MovieId id) {
        return port.getMovie(id);
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    MovieDto createMovie(@NotNull @RequestBody @Valid MovieCreateRequest request) {
        return port.createMovie(request);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/{id:\\d+}")
    MovieDto updateMovie(@PathVariable MovieId id, @NotNull @RequestBody @Valid MovieCreateRequest request) {
        return port.updateMovie(id, request);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id:\\d+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteMovie(@PathVariable MovieId id) {
        port.deleteMovie(id);
    }
}
