package com.flickcrit.app.domain.model.user;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void givenUserWhenGetRolesExpectImmutableCollection() {
        // given
        User user = createUserBuilder()
            .roles(Set.of(UserRole.ADMIN))
            .build();

        // when
        Set<UserRole> roles = user.getRoles();

        // then
        assertThat(roles).isUnmodifiable();
    }

    @Test
    void givenUserWithoutRolesWhenGrantRoleThenRoleIsAdded() {
        // given
        User user = createUserBuilder().build();
        UserRole roleToGrant = UserRole.ADMIN;

        // when
        user.grantRole(roleToGrant);

        // then
        assertTrue(user.getRoles().contains(roleToGrant));
    }

    @Test
    void givenUserWithExistingRoleWhenGrantSameRoleThenRoleIsNotDuplicated() {
        // given
        UserRole existingRole = UserRole.ADMIN;
        User user = createUserBuilder()
            .roles(Set.of(existingRole))
            .build();

        // when
        user.grantRole(existingRole);

        // then
        assertEquals(1, user.getRoles().size(), "The role should not be duplicated.");
        assertTrue(user.getRoles().contains(existingRole), "The existing role should still be present.");
    }

    @Test
    void givenUserWhenGrantNullRoleThenThrowsException() {
        // given
        User user = createUserBuilder().build();

        // when & then
        assertThrows(IllegalArgumentException.class, () -> user.grantRole(null));
    }

    @Test
    void givenUserWithRoleWhenRevokeRoleThenRoleIsRemoved() {
        // given
        UserRole existingRole = UserRole.ADMIN;
        User user = createUserBuilder()
            .roles(Set.of(existingRole))
            .build();

        // when
        user.revokeRole(existingRole);

        // then
        assertFalse(user.getRoles().contains(existingRole));
    }

    @Test
    void givenUserWithoutRoleWhenRevokeRoleThenNoChange() {
        // given
        User user = createUserBuilder().build();
        UserRole roleToRevoke = UserRole.ADMIN;

        // when
        user.revokeRole(roleToRevoke);

        // then
        assertEquals(0, user.getRoles().size());
    }

    @Test
    void givenUserWhenRevokeNullRoleThenThrowsException() {
        // given
        User user = createUserBuilder().build();

        // when & then
        assertThrows(IllegalArgumentException.class, () -> user.revokeRole(null));
    }

    private static User.UserBuilder createUserBuilder() {
        return User.builder()
            .id(UserId.of(1L))
            .email(new Email("user@example.com"))
            .password("password")
            .roles(Set.of());
    }
}