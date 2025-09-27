package com.flickcrit.app.infrastructure.persistence.converter;

import com.flickcrit.app.domain.model.common.EntityId;
import com.flickcrit.app.domain.model.user.User;
import com.flickcrit.app.domain.model.user.UserRole;
import com.flickcrit.app.infrastructure.persistence.model.UserEntity;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class UserToUserEntityConverter implements Converter<User, UserEntity> {

    @Override
    public UserEntity convert(@Nonnull User user) {
        Long userId = Optional.ofNullable(user.getId())
            .map(EntityId::value)
            .orElse(null);

        return UserEntity.builder()
            .id(userId)
            .email(user.getEmail().value())
            .password(user.getPassword())
            .roles(user.getRoles().toArray(new UserRole[0]))
            .build();
    }
}
