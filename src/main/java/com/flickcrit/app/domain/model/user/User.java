package com.flickcrit.app.domain.model.user;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.flickcrit.app.utils.ObjectUtils.requireNonNull;
import static java.util.Objects.requireNonNullElse;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    private final UserId id;

    @EqualsAndHashCode.Include
    private final Email email;
    private final Set<UserRole> roles;
    private String password;

    /**
     * Constructs a new User instance with the specified email and password.
     * A unique UserId is generated for the new User.
     */
    public User(Email email, String password) {
        this(null, email, password, Set.of());
    }

    /**
     * Constructs a new User instance with the specified ID, email, and password.
     * Mostly used to reconstruct a User from a persisted entity.
     */
    @Builder
    User(UserId id, Email email, String password, Set<UserRole> roles) {
        this.id = id;
        this.email = requireNonNull(email, "Email must be defined for a user");
        this.password = requireNonNull(password, "Password must be defined for a user");
        this.roles = new HashSet<>(requireNonNullElse(roles, Set.of()));
    }

    /**
     * Retrieves an unmodifiable view of the user's assigned roles.
     *
     * @return a set containing the current roles of the user
     */
    public Set<UserRole> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    /**
     * Grants the specified role to the user by adding it to the user's roles.
     *
     * @param role the role to be granted to the user; must not be null
     */
    public void grantRole(@NonNull UserRole role) {
        roles.add(role);
    }

    /**
     * Revokes the specified role from the user by removing it from the user's roles.
     *
     * @param role the role to be revoked from the user; must not be null
     */
    public void revokeRole(@NonNull UserRole role) {
        roles.remove(role);
    }
}
