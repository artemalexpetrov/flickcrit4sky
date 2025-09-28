package com.flickcrit.app.infrastructure.security.service;


public interface AuthService {

    /**
     * Authenticates a user using the provided username and password credentials.
     * This method performs the validation process and, upon successful authentication,
     * generates and returns a pair of tokens for secure access and session management.
     *
     * @param username the username of the user attempting to authenticate
     * @param password the password associated with the provided username
     * @return a {@code TokenPair} containing the access token and refresh token upon successful authentication
     */
    TokenPair authenticate(String username, String password);

    /**
     * Refreshes the authentication tokens by using the provided refresh token.
     * This method validates the given refresh token, and if valid, generates and
     * returns a new pair of tokens for continued secure access.
     *
     * @param refreshToken the refresh token used to generate a new token pair
     * @return a {@code TokenPair} containing a new access token and refresh token
     */
    TokenPair refreshToken(String refreshToken);

    /**
     * Encodes the given password using a secure hashing algorithm.
     * This method is typically used to prepare a password for secure storage or comparison during authentication.
     *
     * @param password the plain text password to be encoded
     * @return the encoded representation of the password
     */
    String encodePassword(String password);
}
