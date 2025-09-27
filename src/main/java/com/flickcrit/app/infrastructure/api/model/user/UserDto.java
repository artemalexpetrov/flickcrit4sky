package com.flickcrit.app.infrastructure.api.model.user;

import com.flickcrit.app.domain.model.user.UserRole;
import jakarta.annotation.Nonnull;
import lombok.Builder;

import java.util.Set;

/**
 * Data Transfer Object representing a user.
 * Encapsulates the user's email and a set of user roles.
 */
@Builder
public record UserDto(

    @Nonnull
    Long id,

    @Nonnull
    String email,

    @Nonnull
    Set<UserRole> roles
) {
}
