package com.flickcrit.app.infrastructure.api.controller;

import com.flickcrit.app.infrastructure.api.model.auth.RefreshTokenRequestDto;
import com.flickcrit.app.infrastructure.api.model.auth.SignInRequestDto;
import com.flickcrit.app.infrastructure.api.model.auth.SignUpRequestDto;
import com.flickcrit.app.infrastructure.api.port.AuthPort;
import com.flickcrit.app.infrastructure.security.service.TokenPair;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Authentication management API")
public class AuthControllerV1 {

    private final AuthPort authPort;

    @Operation(summary = "Register a new user", description = "Creates a new user account with the provided credentials")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "User already exists")
    })
    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@Valid @RequestBody SignUpRequestDto signUpRequest) {
        authPort.signUp(signUpRequest);
    }

    @Operation(summary = "Authenticate user", description = "Authenticates user and returns access and refresh tokens")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully authenticated"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/sign-in")
    public TokenPair signIn(@Valid @RequestBody SignInRequestDto signInRequest) {
        return authPort.signIn(signInRequest);
    }

    @Operation(summary = "Refresh token", description = "Generates new token pair using refresh token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "New token pair generated"),
        @ApiResponse(responseCode = "401", description = "Invalid refresh token")
    })
    @PostMapping("/refresh-token")
    public TokenPair refreshToken(@Valid @RequestBody RefreshTokenRequestDto refreshTokenRequest) {
        return authPort.refreshToken(refreshTokenRequest);
    }
}
