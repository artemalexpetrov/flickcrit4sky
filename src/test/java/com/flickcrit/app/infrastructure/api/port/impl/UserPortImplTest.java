package com.flickcrit.app.infrastructure.api.port.impl;

import com.flickcrit.app.domain.exception.EntityNotFoundException;
import com.flickcrit.app.domain.model.user.User;
import com.flickcrit.app.domain.model.user.UserId;
import com.flickcrit.app.domain.service.UserService;
import com.flickcrit.app.infrastructure.api.model.common.PageRequestDto;
import com.flickcrit.app.infrastructure.api.model.common.PageResponse;
import com.flickcrit.app.infrastructure.api.model.user.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserPortImplTest {

    @Mock
    private UserService userServiceMock;

    @Mock
    private ConversionService converterMock;

    @InjectMocks
    private UserPortImpl userPort;

    @Test
    void givenPageableRequestWhenGetUsersExpectPageResponse() {
        // given
        User user = mock(User.class);
        UserDto userDto = mock(UserDto.class);
        PageRequestDto pageRequest = PageRequestDto.of(10, 40);
        Page<User> usersPage = new PageImpl<>(List.of(user));

        when(userServiceMock
            .getUsers(any()))
            .thenReturn(usersPage);

        when(converterMock
            .convert(any(), eq(UserDto.class)))
            .thenReturn(userDto);

        // when
        PageResponse<UserDto> usersDto = userPort.getUsers(pageRequest);

        // then
        assertThat(usersDto).isNotNull();
        assertThat(usersDto.getItems()).containsExactly(userDto);
        verify(userServiceMock).getUsers(assertArg(pageable -> {
            assertEquals(pageRequest.getPage(), pageable.getPageNumber());
            assertEquals(pageRequest.getSize(), pageable.getPageSize());
        }));

        verify(converterMock).convert(user, UserDto.class);
        verifyNoMoreInteractions(userServiceMock, converterMock);
    }

    @Test
    void givenNullablePageRequestWhenGetUsersExpectException() {
        assertThrows(IllegalArgumentException.class, () -> userPort.getUsers(null));
    }

    @Test
    void givenUserWhenGetUserExpectUserDto() {
        // given
        UserId userId = UserId.of(15L);
        User user = mock(User.class);
        UserDto userDto = mock(UserDto.class);

        when(userServiceMock
            .getUser(any(UserId.class)))
            .thenReturn(user);

        when(converterMock
            .convert(any(), eq(UserDto.class)))
            .thenReturn(userDto);

        // when
        UserDto result = userPort.getUser(userId);

        // then
        assertThat(result).isEqualTo(userDto);
        verify(userServiceMock).getUser(userId);
        verify(converterMock).convert(user, UserDto.class);
        verifyNoMoreInteractions(userServiceMock, converterMock);
    }

    @Test
    void givenNonExistentUserWhenGetUserExpectEntityNotFoundException() {
        // given
        UserId userId = UserId.of(99L);
        when(userServiceMock
            .getUser(any(UserId.class)))
            .thenThrow(new EntityNotFoundException("User not found"));

        // when/then
        assertThrows(EntityNotFoundException.class, () -> userPort.getUser(userId));
        verify(userServiceMock).getUser(userId);
        verifyNoMoreInteractions(userServiceMock);
        verifyNoInteractions(converterMock);
    }

    @Test
    void givenNullableUserIdWhenGetUserExpectException() {
        assertThrows(IllegalArgumentException.class, () -> userPort.getUser(null));
    }

    @Test
    void givenExistingUserWhenDeleteUserExpectSuccessfulDeletion() {
        // given
        UserId userId = UserId.of(1L);
        User user = mock(User.class);

        when(userServiceMock
            .getUser(any(UserId.class)))
            .thenReturn(user);

        // when
        userPort.deleteUser(userId);

        // then
        verify(userServiceMock).getUser(userId);
        verify(userServiceMock).delete(user);
        verifyNoMoreInteractions(userServiceMock);
        verifyNoInteractions(converterMock);
    }

    @Test
    void givenNonExistentUserWhenDeleteUserExpectEntityNotFoundException() {
        // given
        UserId userId = UserId.of(99L);

        when(userServiceMock
            .getUser(any(UserId.class)))
            .thenThrow(new EntityNotFoundException("User not found"));

        // when/then
        assertThrows(EntityNotFoundException.class, () -> userPort.deleteUser(userId));
        verify(userServiceMock).getUser(userId);
        verifyNoMoreInteractions(userServiceMock);
        verifyNoInteractions(converterMock);
    }

    @Test
    void givenNullableUserIdWhenDeleteUserExpectException() {
        assertThrows(IllegalArgumentException.class, () -> userPort.deleteUser(null));
    }
}