package com.flickcrit.app.infrastructure.security.service;

import com.flickcrit.app.utils.ObjectUtils;

/**
 * Represents a pair of tokens typically used in authentication workflows.
 * This class encapsulates an access token and a refresh token, where the access token
 * is primarily used for authenticated requests, and the refresh token is used to
 * obtain new access tokens when the current one expires.
 *
 * @param accessToken the access token, which cannot be null
 * @param refreshToken the refresh token, which may be null
 */
public record TokenPair(
    String accessToken,
    String refreshToken) {

    public TokenPair {
        ObjectUtils.requireNonNull(accessToken, "Access token cannot be null");
    }
}
