package com.flickcrit.app.infrastructure.security.service.impl;

import com.flickcrit.app.infrastructure.security.config.JwtProperties;
import com.flickcrit.app.infrastructure.security.service.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenServiceTest {

    private final JwtProperties properties = new JwtProperties("secret".repeat(10), 180, 360);
    private JwtTokenService jwtTokenService;

    @BeforeEach
    void setUp() {
        jwtTokenService = new JwtTokenService(properties);
    }

    @Test
    void givenUserDetailsWhenIssueTokenExpectValidTokenPair() {
        // given
        UserDetails userDetails = buildUserDetails();

        // when
        var tokenPair = jwtTokenService.issueToken(userDetails);

        // then
        assertNotNull(tokenPair);
        assertNotNull(tokenPair.accessToken());
        assertNotNull(tokenPair.refreshToken());

        Token accessToken = jwtTokenService.parseToken(tokenPair.accessToken());
        Token refreshToken = jwtTokenService.parseToken(tokenPair.refreshToken());

        assertNotNull(accessToken);
        assertEquals(userDetails.getUsername(), accessToken.getUsername());
        assertTrue(accessToken.isAccessToken());
        assertFalse(accessToken.isExpired());

        assertNotNull(refreshToken);
        assertEquals(userDetails.getUsername(), refreshToken.getUsername());
        assertTrue(accessToken.isRefreshToken());
        assertFalse(refreshToken.isExpired());
    }

    @Test
    void givenValidTokenWhenParseTokenExpectValidToken() {
        // given
        UserDetails userDetails = buildUserDetails();
        var tokenPair = jwtTokenService.issueToken(userDetails);

        // when
        var token = jwtTokenService.parseToken(tokenPair.accessToken());

        // then
        assertNotNull(token);
        assertEquals(userDetails.getUsername(), token.getUsername());
        assertFalse(token.isExpired());
    }

    @Test
    void givenInvalidTokenWhenParseTokenExpectBadCredentialsException() {
        // when
        assertThrows(BadCredentialsException.class, () -> jwtTokenService.parseToken("invalid.token.string"));
    }

    private UserDetails buildUserDetails() {
        return User.builder()
            .username("test@example.com")
            .password("password")
            .authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
            .build();
    }
}