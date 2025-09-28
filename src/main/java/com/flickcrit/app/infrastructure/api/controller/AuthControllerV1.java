package com.flickcrit.app.infrastructure.api.controller;

import com.flickcrit.app.infrastructure.api.model.auth.RefreshTokenRequestDto;
import com.flickcrit.app.infrastructure.api.model.auth.SignInRequestDto;
import com.flickcrit.app.infrastructure.api.model.auth.SignUpRequestDto;
import com.flickcrit.app.infrastructure.api.port.AuthPort;
import com.flickcrit.app.infrastructure.security.service.TokenPair;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthControllerV1 {

    private final AuthPort authPort;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequestDto signUpRequest) {
        authPort.signUp(signUpRequest);
        return ResponseEntity.ok().build();
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
