package com.flickcrit.app.infrastructure.api.model.auth;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public record RefreshTokenRequestDto(

    @NotBlank
    String refreshToken
) {
}
