package com.flickcrit.app.domain.service.impl;


import com.flickcrit.app.domain.exception.EntityNotFoundException;
import com.flickcrit.app.domain.model.user.Email;
import com.flickcrit.app.domain.model.user.User;
import com.flickcrit.app.domain.model.user.UserId;
import com.flickcrit.app.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repositoryMock;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void givenUserWhenGetUserByIdExpectUser() {
        // given
        UserId userId = UserId.of(10L);
        User expectedUser = mock(User.class);

        when(repositoryMock
            .findById(any()))
            .thenReturn(Optional.of(expectedUser));

        // when
        User actualUser = userService.getUser(userId);

        // then
        assertEquals(expectedUser, actualUser);
        verify(repositoryMock).findById(userId);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void givenNoUserWhenGetUserByIdExpectException() {
        // given
        UserId userId = UserId.of(10L);
        when(repositoryMock
            .findById(userId))
            .thenReturn(Optional.empty());

        // when / then
        assertThrows(EntityNotFoundException.class, () -> userService.getUser(userId));
        verify(repositoryMock).findById(userId);
    }

    @Test
    void givenUserWhenGetUserByEmailExpectUser() {
        // given
        Email email = Email.of("test@example.com");
        User expectedUser = mock(User.class);

        when(repositoryMock
            .findByEmail(email))
            .thenReturn(Optional.of(expectedUser));

        // when
        User actualUser = userService.getUser(email);

        // then
        assertEquals(expectedUser, actualUser);
        verify(repositoryMock).findByEmail(email);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void givenNoUserWhenGetUserByEmailExpectException() {
        // given
        Email email = Email.of("test@example.com");
        when(repositoryMock.findByEmail(email))
            .thenReturn(Optional.empty());

        // when / then
        assertThrows(EntityNotFoundException.class, () -> userService.getUser(email));
        verify(repositoryMock).findByEmail(email);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void givenUsersWhenGetUsersWithPageableExpectPageOfUsers() {
        // given
        Pageable pageable = Pageable.ofSize(10);
        List<User> expectedUsers = List.of(mock(User.class));
        Page<User> expectedPage = new PageImpl<>(expectedUsers);

        when(repositoryMock
            .findAll(any()))
            .thenReturn(expectedPage);

        // when
        Page<User> actualPage = userService.getUsers(pageable);

        // then
        assertEquals(expectedPage, actualPage);
        verify(repositoryMock).findAll(pageable);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void givenUserWhenSaveUserExpectSavedUser() {
        // given
        User userToSave = mock(User.class);
        User expectedUser = mock(User.class);

        when(repositoryMock
            .save(any()))
            .thenReturn(expectedUser);

        // when
        User savedUser = userService.save(userToSave);

        // then
        assertEquals(expectedUser, savedUser);
        verify(repositoryMock).save(userToSave);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void givenNullUserWhenSaveUserExpectException() {
        // when / then
        assertThrows(IllegalArgumentException.class, () -> userService.save(null));
        verifyNoInteractions(repositoryMock);
    }

    @Test
    void givenExistingUserIdWhenDeleteUserExpectSuccess() {
        // given
        User userToDelete = mock(User.class);

        // when
        userService.delete(userToDelete);

        // then
        verify(repositoryMock).delete(userToDelete);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void givenNullUserIdWhenDeleteUserExpectException() {
        // when / then
        assertThrows(IllegalArgumentException.class, () -> userService.delete(null));
        verifyNoInteractions(repositoryMock);
    }
}