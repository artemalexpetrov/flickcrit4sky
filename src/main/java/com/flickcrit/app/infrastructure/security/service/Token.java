package com.flickcrit.app.infrastructure.security.service;

import jakarta.annotation.Nullable;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

/**
 * Represents a token that encapsulates authentication-related information, such as a username.
 * This interface provides methods to retrieve and validate the presence of a username associated
 * with the token. Implementations of this interface typically represent parsed or processed security
 * tokens used in authentication workflows.
 */
public interface Token {

    /**
     * Retrieves the username associated with this token.
     *
     * @return the username as a String, or null if no username is associated with the token.
     */
    @Nullable
    String getUsername();

    /**
     * Checks if a username is associated with this token.
     *
     * @return true if a username is present, false otherwise.
     */
    boolean hasUsername();

    /**
     * Checks if the token is expired.
     *
     * @return true if the token is expired, false otherwise.
     */
    boolean isExpired();

    /**
     * Retrieves the set of authorities granted to this token.
     * Authorities typically represent roles or permissions associated
     * with the token and may be used to determine access control.
     *
     * @return a set of {@code GrantedAuthority} associated with this token,
     *         or an empty set if no authorities are granted.
     */
    Set<GrantedAuthority> getAuthorities();
}
