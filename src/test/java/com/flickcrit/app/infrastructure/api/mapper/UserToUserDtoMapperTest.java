package com.flickcrit.app.infrastructure.api.mapper;


import com.flickcrit.app.domain.model.user.Email;
import com.flickcrit.app.domain.model.user.User;
import com.flickcrit.app.domain.model.user.UserId;
import com.flickcrit.app.domain.model.user.UserRole;
import com.flickcrit.app.infrastructure.api.model.user.UserDto;
import com.flickcrit.app.infrastructure.core.conversion.mapper.EntityIdMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserToUserDtoMapperTest {

    private UserToUserDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserToUserDtoMapperImpl(new EntityIdMapper());
    }

    @Test
    void givenUserWhenConvertToDtoExpectDtoWithSameValues() {
        // given
        User user = User.builder()
            .id(UserId.of(1L))
            .password("encrypted")
            .email(Email.of("test@example.com"))
            .roles(Set.of(UserRole.USER, UserRole.ADMIN))
            .build();

        // when
        UserDto result = mapper.convert(user);

        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(user.getId().value());
        assertThat(result.email()).isEqualTo(user.getEmail().value());
        assertThat(result.roles()).isEqualTo(user.getRoles());
    }
}