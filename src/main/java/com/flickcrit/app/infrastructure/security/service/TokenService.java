package com.flickcrit.app.infrastructure.security.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface TokenService {

    /**
     * Issues a new pair of tokens, including an access token and a refresh token,
     * based on the provided user details. This method is typically used in the
     * authentication and session management process.
     *
     * @param userDetails the user details for which the token pair is to be issued
     * @return a {@code TokenPair} containing the issued access token and refresh token
     */
    TokenPair issueToken(UserDetails userDetails);

    /**
     * Parses the provided token string and returns a {@code Token} object that represents
     * the parsed token. The returned token may encapsulate various authentication-related
     * information, such as a username, and can provide validation methods like checking for expiration.
     *
     * @param token the token string to be parsed
     * @return the parsed {@code Token} object
     * @throws org.springframework.security.core.AuthenticationException if the token cannot be parsed or is invalid
     */
    Token parseToken(String token);

}
