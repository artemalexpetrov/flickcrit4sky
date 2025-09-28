package com.flickcrit.app.infrastructure.api.port.impl;

import com.flickcrit.app.domain.model.user.Email;
import com.flickcrit.app.domain.service.UserService;
import com.flickcrit.app.infrastructure.api.model.auth.RefreshTokenRequestDto;
import com.flickcrit.app.infrastructure.api.model.auth.SignInRequestDto;
import com.flickcrit.app.infrastructure.api.model.auth.SignUpRequestDto;
import com.flickcrit.app.infrastructure.api.port.AuthPort;
import com.flickcrit.app.infrastructure.security.service.AuthService;
import com.flickcrit.app.infrastructure.security.service.TokenPair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthPortImpl implements AuthPort {

    private final AuthService authService;
    private final UserService userService;

    @Override
    public void signUp(SignUpRequestDto signUpRequest) {
        String encodedPassword = authService.encodePassword(signUpRequest.password());
        userService.createUser(Email.of(signUpRequest.email()), encodedPassword);
    }

    @Override
    public TokenPair signIn(SignInRequestDto signInRequest) {
        return authService.authenticate(signInRequest.email(), signInRequest.password());
    }

    @Override
    public TokenPair refreshToken(RefreshTokenRequestDto refreshToken) {
        return authService.refreshToken(refreshToken.refreshToken());
    }
}
