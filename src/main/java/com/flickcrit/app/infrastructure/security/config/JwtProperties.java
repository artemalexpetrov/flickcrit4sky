package com.flickcrit.app.infrastructure.security.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@Getter
@Validated
@RequiredArgsConstructor(onConstructor_ = @ConstructorBinding)
@ConfigurationProperties(prefix = "jwt")
public final class JwtProperties {

    @NotBlank
    @Length(min = 64, message = "The specified key is too short for HMAC-SHA algorithm")
    private final String secretKey;

    @Min(1)
    private final long accessTokenExpirationTimeSeconds;
    private final long refreshTokenExpirationTimeSeconds;
}
