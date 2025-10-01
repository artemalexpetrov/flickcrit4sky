package com.flickcrit.app.infrastructure.api.controller;

import com.flickcrit.app.BaseSpringBootIT;
import com.flickcrit.app.domain.model.movie.Isan;
import com.flickcrit.app.domain.model.movie.Movie;
import com.flickcrit.app.domain.repository.MovieRepository;
import com.flickcrit.app.infrastructure.api.model.movie.MovieCreateRequest;
import com.flickcrit.app.infrastructure.api.model.movie.MovieDto;
import com.flickcrit.app.infrastructure.cache.config.CacheRegions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.Year;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class MovieControllerV1CacheIT extends BaseSpringBootIT {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieControllerV1 controller;

    @Autowired
    private CacheManager cacheManager;

    @Test
    void givenMovieWhenGetMovieExpectMovieFromCache() {
        // given
        Movie movie = movieRepository.save(buildMovie());
        MovieDto movieDto = controller.getMovie(movie.getId());

        // when
        MovieDto cachedMovie = getMovieCache().get(movie.getId(), MovieDto.class);

        // then
        assertThat(movieDto).isEqualTo(cachedMovie);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void givenMovieWhenSaveMovieExpectCacheEvicted() {
        // given
        Movie movie = movieRepository.save(buildMovie());
        MovieDto movieDto = controller.getMovie(movie.getId());

        assertThat(movieDto).isNotNull();
        assertNotNull(getMovieCache().get(movie.getId()));

        // when
        controller.updateMovie(movie.getId(), MovieCreateRequest.builder()
            .title("Updated Movie")
            .year(2012)
            .build());

        // then
        assertNull(getMovieCache().get(movie.getId()));
    }

    private Cache getMovieCache() {
        Cache cache = cacheManager.getCache(CacheRegions.MOVIES);
        assertNotNull(cache);
        return cache;
    }

    private static Movie buildMovie() {
        return Movie.builder()
            .title("Test Movie")
            .isan(Isan.generate())
            .year(Year.of(2025))
            .build();
    }
}
