package com.flickcrit.app.infrastructure.api.model.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public record SignInRequestDto(

    @Email
    @NotBlank(message = "Email is required")
    String email,

    @NotBlank
    String password
) {
}
