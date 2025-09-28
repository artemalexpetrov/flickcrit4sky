package com.flickcrit.app.infrastructure.api.controller;

import com.flickcrit.app.infrastructure.api.model.auth.RefreshTokenRequestDto;
import com.flickcrit.app.infrastructure.api.model.auth.SignInRequestDto;
import com.flickcrit.app.infrastructure.api.model.auth.SignUpRequestDto;
import com.flickcrit.app.infrastructure.api.port.AuthPort;
import com.flickcrit.app.infrastructure.security.service.TokenPair;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthControllerV1 {

    private final AuthPort authPort;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@Valid @RequestBody SignUpRequestDto signUpRequest) {
        authPort.signUp(signUpRequest);
    }

    @PostMapping("/sign-in")
    public TokenPair signIn(@Valid @RequestBody SignInRequestDto signInRequest) {
        return authPort.signIn(signInRequest);
    }

    @PostMapping("/refresh-token")
    public TokenPair refreshToken(@Valid @RequestBody RefreshTokenRequestDto refreshTokenRequest) {
        return authPort.refreshToken(refreshTokenRequest);
    }
}
