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
        Token token = new JwtTokenAdapter(createTestToken(username, roles, expiresInMillis));

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
        Token token = new JwtTokenAdapter(createTestToken("test", List.of(), expiresInMillis));

        // when / then
        assertNotNull(token);
        Awaitility.await()
            .atMost(expiresInMillis + validationOffsetMillis, TimeUnit.MILLISECONDS)
            .until(token::isExpired);
    }

    private Jws<Claims> createTestToken(String username, List<String> roles, long expiresInMillis) {
        String rawJwt = Jwts.builder()
            .subject(username)
            .issuedAt(new Date())
            .claim(JwtTokenAdapter.AUTHORITIES_KEY, String.join(",", roles))
            .expiration(Date.from(Instant.now().plusMillis(expiresInMillis)))
            .signWith(key)
            .compact();

        return Jwts.parser()
            .verifyWith(key).build()
            .parseSignedClaims(rawJwt);
    }
}