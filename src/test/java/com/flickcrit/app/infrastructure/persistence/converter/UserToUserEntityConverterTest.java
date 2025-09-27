package com.flickcrit.app.infrastructure.persistence.converter;

import com.flickcrit.app.domain.model.user.Email;
import com.flickcrit.app.domain.model.user.User;
import com.flickcrit.app.domain.model.user.UserId;
import com.flickcrit.app.domain.model.user.UserRole;
import com.flickcrit.app.infrastructure.persistence.model.UserEntity;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserToUserEntityConverterTest {

    private final UserToUserEntityConverter converter = new UserToUserEntityConverter();

    @Test
    void givenUserWhenConvertToEntityExpectUserEntity() {
        // given
        User testUser = createUserBuilder().build();

        // when
        UserEntity result = converter.convert(testUser);

        // then
        assertNotNull(result);
        assertEquals(testUser.getId().value(), result.getId());
        assertEquals(testUser.getEmail().value(), result.getEmail());
        assertEquals(testUser.getPassword(), result.getPassword());
        assertArrayEquals(new UserRole[]{UserRole.USER}, result.getRoles());
    }

    @Test
    void givenUserWithoutIdWhenConvertThenReturnsUserEntityWithoutId() {
        // given
        User user = createUserBuilder().id(null).build();

        // when
        UserEntity userEntity = converter.convert(user);

        // then
        assertNotNull(userEntity);
        assertNull(userEntity.getId());
    }

    private static User.UserBuilder createUserBuilder() {
        return User.builder()
            .id(UserId.of(19L))
            .email(Email.of("test@example.com"))
            .password("password123")
            .roles(Set.of(UserRole.USER));
    }
}