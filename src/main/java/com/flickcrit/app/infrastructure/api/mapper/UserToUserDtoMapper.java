package com.flickcrit.app.infrastructure.api.mapper;

import com.flickcrit.app.domain.model.user.User;
import com.flickcrit.app.infrastructure.api.model.user.UserDto;
import com.flickcrit.app.infrastructure.core.conversion.config.MapStructConfig;
import jakarta.annotation.Nonnull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapStructConfig.class)
interface UserToUserDtoMapper extends Converter<User, UserDto> {

    @Override
    @Mapping(target = "email", source = "email.value")
    UserDto convert(@Nonnull User user);
}
