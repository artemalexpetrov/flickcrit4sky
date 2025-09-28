package com.flickcrit.app.infrastructure.api.model.rating;

import jakarta.annotation.Nonnull;

import java.math.BigDecimal;

public record AverageRatingDto(
    @Nonnull
    BigDecimal value,

    @Nonnull
    Long ratesCount) {
}
