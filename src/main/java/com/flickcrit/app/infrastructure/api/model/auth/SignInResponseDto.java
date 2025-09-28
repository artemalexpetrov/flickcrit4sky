package com.flickcrit.app.infrastructure.api.model.auth;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record SignInResponseDto(
    @Nonnull
    String accessToken,

    @Nullable
    String refreshToken
) {
}
