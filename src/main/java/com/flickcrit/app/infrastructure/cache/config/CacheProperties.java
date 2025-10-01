package com.flickcrit.app.infrastructure.cache.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.time.DurationMax;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Getter
@Validated
@RequiredArgsConstructor(onConstructor_ = @ConstructorBinding)
@ConfigurationProperties(prefix = "cache")
public class CacheProperties {

    @DurationMax(days = 10, message = "The maximum cache TTL is {days} days")
    @DurationMin(seconds = 10, message = "The minimum cache TTL is {seconds} seconds")
    private final Duration moviesCacheTtl;

    @DurationMax(days = 10, message = "The maximum cache TTL is {days} days")
    @DurationMin(seconds = 10, message = "The minimum cache TTL is {seconds} seconds")
    private final Duration moviesRatingsCacheTtl;

}
