package com.flickcrit.app.infrastructure.cache.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@EnableCaching
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(CacheProperties.class)
public class CaffeineCacheConfig {

    private static final int MAXIMUM_CACHE_SIZE = 1000;

    private final CacheProperties properties;

    @Bean
    CacheManager caffeineCacheManager() {
        CaffeineCache moviesCache = new CaffeineCache(CacheRegions.MOVIES, Caffeine.newBuilder()
            .expireAfterWrite(properties.getMoviesCacheTtl())
            .maximumSize(MAXIMUM_CACHE_SIZE)
            .build());

        CaffeineCache ratingsCache = new CaffeineCache(CacheRegions.MOVIES, Caffeine.newBuilder()
            .expireAfterWrite(properties.getMoviesRatingsCacheTtl())
            .maximumSize(1000)
            .build());

        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(List.of(moviesCache, ratingsCache));
        return cacheManager;
    }
}
