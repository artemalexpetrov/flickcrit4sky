package com.flickcrit.app.infrastructure.security.service.impl;

import com.flickcrit.app.infrastructure.security.service.AuthService;
import com.flickcrit.app.infrastructure.security.service.Token;
import com.flickcrit.app.infrastructure.security.service.TokenPair;
import com.flickcrit.app.infrastructure.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class AuthServiceImpl implements AuthService {

    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    @Override
    public TokenPair authenticate(String username, String password) {
        UserDetails userDetails = doAuthenticate(username, password);
        return tokenService.issueToken(userDetails);
    }

    @Override
    public TokenPair refreshToken(String refreshToken) {
        Token token = tokenService.parseToken(refreshToken);
        if (token == null || token.isExpired()) {
            throw new BadCredentialsException("Token expired");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(token.getUsername());
        return tokenService.issueToken(userDetails);
    }

    @Override
    public String encodePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be blank");
        }

        return passwordEncoder.encode(password);
    }

    private UserDetails doAuthenticate(String username, String password) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(auth);

        return (UserDetails) authentication.getPrincipal();
    }
}
