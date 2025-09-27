package com.flickcrit.app.infrastructure.persistence.converter;

import com.flickcrit.app.domain.model.user.User;
import com.flickcrit.app.domain.model.user.UserRole;
import com.flickcrit.app.infrastructure.persistence.model.UserEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserEntityToUserConverterTest {

    private final UserEntityToUserConverter converter = new UserEntityToUserConverter();

    @Test
    void givenUserEntityWhenConvertExpectUser() {
        // given
        UserEntity entity = UserEntity.builder()
            .id(1L)
            .email("test@example.com")
            .password("password123")
            .roles(new UserRole[]{UserRole.ADMIN})
            .build();

        // when
        User user = converter.convert(entity);

        // then
        assertThat(user).isNotNull();
        assertThat(user.getId().value()).isEqualTo(entity.getId());
        assertThat(user.getEmail().value()).isEqualTo(entity.getEmail());
        assertThat(user.getPassword()).isEqualTo(entity.getPassword());
        assertThat(user.getRoles()).containsExactlyInAnyOrder(entity.getRoles());
    }
}