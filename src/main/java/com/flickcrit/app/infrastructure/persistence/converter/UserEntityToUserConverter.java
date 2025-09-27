package com.flickcrit.app.infrastructure.persistence.converter;

import com.flickcrit.app.domain.model.user.Email;
import com.flickcrit.app.domain.model.user.User;
import com.flickcrit.app.domain.model.user.UserId;
import com.flickcrit.app.infrastructure.persistence.model.UserEntity;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
class UserEntityToUserConverter implements Converter<UserEntity, User> {

    @Override
    public User convert(@Nonnull UserEntity entity) {
        return User.builder()
            .id(UserId.optional(entity.getId()))
            .email(new Email(entity.getEmail()))
            .password(entity.getPassword())
            .roles(Set.of(entity.getRoles()))
            .build();
    }
}
