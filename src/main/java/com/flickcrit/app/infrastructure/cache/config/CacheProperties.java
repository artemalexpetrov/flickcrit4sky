package com.flickcrit.app.infrastructure.cache.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.time.DurationMax;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Getter
@Setter
@Validated
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "cache")
public class CacheProperties {

    @DurationMax(days = 10, message = "The maximum cache TTL is {days} days")
    @DurationMin(seconds = 5, message = "The minimum cache TTL is {seconds} seconds")
    private Duration moviesCacheTtl = Duration.ofMinutes(10);

    @DurationMax(days = 10, message = "The maximum cache TTL is {days} days")
    @DurationMin(seconds = 5, message = "The minimum cache TTL is {seconds} seconds")
    private Duration moviesRatingsCacheTtl = Duration.ofSeconds(10);

}
