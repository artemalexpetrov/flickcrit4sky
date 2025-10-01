package com.flickcrit.app.infrastructure.security.service.impl;

import com.flickcrit.app.infrastructure.security.service.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenAdapterTest {

    private static final String SECRET_KEY = "secret".repeat(10);

    private SecretKey key;

    @BeforeEach
    void setUp() {
        key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    @Test
    void givenValidTokenWhenParseTokenThenReturnToken() {
        // given
        long expiresInMillis = 30000;
        String username = "test@example.com";
        List<String> roles = List.of("ROLE_USER");
        Token token = new JwtTokenAdapter(createAccessToken(username, roles, expiresInMillis));

        // when / then
        assertNotNull(token);
        assertEquals(username, token.getUsername());
        assertThat(token.getAuthorities())
            .extracting(GrantedAuthority::getAuthority)
            .containsExactlyInAnyOrderElementsOf(roles);
        assertFalse(token.isExpired());
    }

    @Test
    void givenExpiredTokenWhenParseTokenThenReturnToken() {
        // given
        int expiresInMillis = 1000;
        int validationOffsetMillis = 1000;
        Token token = new JwtTokenAdapter(createAccessToken("test", List.of(), expiresInMillis));

        // when / then
        assertNotNull(token);
        Awaitility.await()
            .atMost(expiresInMillis + validationOffsetMillis, TimeUnit.MILLISECONDS)
            .until(token::isExpired);
    }

    @Test
    void givenTokenWhenIsAccessTokenExpectCorrectAnswer() {
        // given
        Token accessToken = new JwtTokenAdapter(createAccessToken("test@user.com", List.of(), 300000));
        Token refreshToken = new JwtTokenAdapter(createRefreshToken("test@user.com", 300000));

        // when / then
        assertNotNull(accessToken);
        assertTrue(accessToken.isAccessToken());
        assertFalse(refreshToken.isAccessToken());
    }

    @Test
    void givenTokenWhenIsRefreshTokenExpectCorrectAnswer() {
        // given
        Token accessToken = new JwtTokenAdapter(createAccessToken("test@user.com", List.of(), 300000));
        Token refreshToken = new JwtTokenAdapter(createRefreshToken("test@user.com", 300000));

        // when / then
        assertNotNull(accessToken);
        assertTrue(refreshToken.isRefreshToken());
        assertFalse(accessToken.isRefreshToken());
    }

    private Jws<Claims> createAccessToken(String username, List<String> roles, long expiresInMillis) {
        Map<String, String> claims = Map.of(
            JwtTokenAdapter.AUTHORITIES_KEY, String.join(",", roles),
            JwtTokenAdapter.TOKEN_TYPE_KEY, JwtTokenAdapter.ACCESS_TOKEN_TYPE
        );

        return createTestToken(username, claims, expiresInMillis);
    }

    private Jws<Claims> createRefreshToken(String username, long expiresInMillis) {
        Map<String, String> claims = Map.of(JwtTokenAdapter.TOKEN_TYPE_KEY, JwtTokenAdapter.REFRESH_TOKEN_TYPE);
        return createTestToken(username, claims, expiresInMillis);
    }

    private Jws<Claims> createTestToken(String username, Map<String, ?> claims, long expiresInMillis) {
        String rawJwt = Jwts.builder()
            .expiration(Date.from(Instant.now().plusMillis(expiresInMillis)))
            .issuedAt(new Date())
            .subject(username)
            .claims(claims)
            .signWith(key)
            .compact();

        return Jwts.parser()
            .verifyWith(key).build()
            .parseSignedClaims(rawJwt);
    }

}