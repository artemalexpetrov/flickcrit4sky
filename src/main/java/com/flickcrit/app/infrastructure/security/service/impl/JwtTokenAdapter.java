package com.flickcrit.app.infrastructure.security.service.impl;

import com.flickcrit.app.infrastructure.security.service.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class JwtTokenAdapter implements Token {

    static final String TOKEN_TYPE_KEY = "type";
    static final String AUTHORITIES_KEY = "authorities";
    static final String AUTHORITIES_DELIMITER = ",";
    static final String ACCESS_TOKEN_TYPE = "access";
    static final String REFRESH_TOKEN_TYPE = "refresh";

    private final Jwt<?, Claims> jwt;

    @Override
    public @Nullable String getUsername() {
        return jwt.getPayload().getSubject();
    }

    @Override
    public boolean hasUsername() {
        return getUsername() != null;
    }

    @Override
    public boolean isExpired() {
        return jwt.getPayload()
            .getExpiration()
            .before(new Date());
    }

    @Override
    public boolean isAccessToken() {
        return jwt.getPayload()
            .get(TOKEN_TYPE_KEY, String.class)
            .equals(ACCESS_TOKEN_TYPE);
    }

    @Override
    public boolean isRefreshToken() {
        return jwt.getPayload()
            .get(TOKEN_TYPE_KEY, String.class)
            .equals(REFRESH_TOKEN_TYPE);
    }

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        String rawAuthorities = jwt.getPayload().get(AUTHORITIES_KEY, String.class);
        return Arrays.stream(rawAuthorities.split(AUTHORITIES_DELIMITER))
            .map(String::trim)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet());
    }
}
