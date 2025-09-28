package com.flickcrit.app.infrastructure.api.port;

import com.flickcrit.app.infrastructure.api.model.auth.RefreshTokenRequestDto;
import com.flickcrit.app.infrastructure.api.model.auth.SignInRequestDto;
import com.flickcrit.app.infrastructure.api.model.auth.SignUpRequestDto;
import com.flickcrit.app.infrastructure.security.service.TokenPair;

/**
 * Provides authentication-related operations including user sign-up, sign-in,
 * token refresh, and sign-out.
 */
public interface AuthPort {

    /**
     * Authenticates a user using the provided sign-in request details.
     * If the authentication is successful, a TokenPair object containing
     * an access token and a refresh token will be returned.
     *
     * @param signInRequest the details required for user authentication, including email and password
     * @return a TokenPair containing an access token and a refresh token
     */
    TokenPair signIn(SignInRequestDto signInRequest);

    /**
     * Refreshes the authentication token using the provided refresh token details.
     * This operation generates a new access token and may replace the existing refresh token.
     *
     * @param refreshTokenRequest the details required to refresh the token, including the refresh token
     * @return a TokenPair containing a new access token and potentially a new refresh token
     */
    TokenPair refreshToken(RefreshTokenRequestDto refreshTokenRequest);

    /**
     * Creates a new user account based on the provided sign-up request details.
     * The request must include valid email, password, and matching password confirmation fields.
     *
     * @param signUpRequest the details required for creating a new user account, including email, password,
     *                      and password confirmation
     */
    void signUp(SignUpRequestDto signUpRequest);
}
