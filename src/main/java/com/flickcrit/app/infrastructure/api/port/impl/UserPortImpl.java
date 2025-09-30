package com.flickcrit.app.infrastructure.api.port.impl;

import com.flickcrit.app.domain.model.user.User;
import com.flickcrit.app.domain.model.user.UserId;
import com.flickcrit.app.domain.service.UserService;
import com.flickcrit.app.infrastructure.api.model.common.PageRequestDto;
import com.flickcrit.app.infrastructure.api.model.common.PageResponse;
import com.flickcrit.app.infrastructure.api.model.user.UserDto;
import com.flickcrit.app.infrastructure.api.port.UserPort;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class UserPortImpl implements UserPort {

    private final UserService userService;
    private final ConversionService converter;

    @Override
    public UserDto getUser(@NonNull UserId id) {
        User user = userService.getUser(id);
        return convertToDto(user);
    }

    @Override
    public PageResponse<UserDto> getUsers(@NonNull PageRequestDto pageRequest) {
        Page<UserDto> usersPage = userService
            .getUsers(PageRequest.of(pageRequest.getPage(), pageRequest.getSize()))
            .map(this::convertToDto);
        
        return PageResponse.of(usersPage);
    }

    @Override
    public void deleteUser(@NonNull UserId id) {
        User user = userService.getUser(id);
        userService.delete(user);
    }

    private UserDto convertToDto(User user) {
        return converter.convert(user, UserDto.class);
    }
}
