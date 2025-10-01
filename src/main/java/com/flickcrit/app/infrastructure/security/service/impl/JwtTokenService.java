package com.flickcrit.app.infrastructure.security.service.impl;

import com.flickcrit.app.infrastructure.security.config.JwtProperties;
import com.flickcrit.app.infrastructure.security.service.Token;
import com.flickcrit.app.infrastructure.security.service.TokenPair;
import com.flickcrit.app.infrastructure.security.service.TokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class JwtTokenService implements TokenService {

    private final JwtProperties jwtProperties;

    @Override
    public TokenPair issueToken(UserDetails userDetails) {
        String refreshToken = createRefreshToken(userDetails);
        String accessToken = createAccessToken(userDetails);

        return new TokenPair(accessToken, refreshToken);
    }

    @Override
    public Token parseToken(String rawToken) {
        try {
            JwtParser parser = Jwts.parser()
                .verifyWith(getSignKey())
                .build();

            Jws<Claims> claimsJws = parser.parseSignedClaims(rawToken);
            return new JwtTokenAdapter(claimsJws);
        } catch (Exception e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }

    private String createAccessToken(UserDetails userDetails) {
        String userAuthorities = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(JwtTokenAdapter.AUTHORITIES_DELIMITER));

        return preBuild(userDetails, jwtProperties.getAccessTokenExpirationTimeSeconds())
            .claims(Map.of(
                JwtTokenAdapter.AUTHORITIES_KEY, userAuthorities,
                JwtTokenAdapter.TOKEN_TYPE_KEY, JwtTokenAdapter.ACCESS_TOKEN_TYPE
            )).compact();
    }

    private String createRefreshToken(UserDetails userDetails) {
        return preBuild(userDetails, jwtProperties.getRefreshTokenExpirationTimeSeconds())
            .claims(Map.of(JwtTokenAdapter.TOKEN_TYPE_KEY, JwtTokenAdapter.REFRESH_TOKEN_TYPE))
            .compact();
    }

    private JwtBuilder preBuild(UserDetails user, long expirationTimeSeconds) {
        return Jwts.builder()
            .subject(user.getUsername())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(getExpirationDate(expirationTimeSeconds))
            .signWith(getSignKey(), Jwts.SIG.HS256);
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private static Date getExpirationDate(long offsetSeconds) {
        return new Date(System.currentTimeMillis() + (offsetSeconds * 1000));
    }
}
