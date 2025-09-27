package com.flickcrit.app.infrastructure.persistence.repository;

import com.flickcrit.app.domain.model.user.Email;
import com.flickcrit.app.domain.model.user.User;
import com.flickcrit.app.domain.model.user.UserId;
import com.flickcrit.app.infrastructure.persistence.model.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    @Mock
    private JpaUserRepository jpaRepositoryMock;

    @Mock
    private ConversionService converterMock;

    @InjectMocks
    private UserRepositoryImpl repository;

    @Test
    void findByIdWhenUserExistsReturnsUser() {
        // given
        UserId userId = UserId.of(1L);
        UserEntity userEntity = mock(UserEntity.class);
        User expectedUser = mock(User.class);

        when(jpaRepositoryMock
            .findById(any()))
            .thenReturn(Optional.of(userEntity));

        when(converterMock
            .convert(eq(userEntity), eq(User.class)))
            .thenReturn(expectedUser);

        // when
        Optional<User> result = repository.findById(userId);

        // then
        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());
        verify(jpaRepositoryMock).findById(userId.value());
        verify(converterMock).convert(userEntity, User.class);
        verifyNoMoreInteractions(jpaRepositoryMock, converterMock);
    }

    @Test
    void findByIdWhenUserDoesNotExistReturnsEmptyOptional() {
        // given
        UserId userId = UserId.of(1L);
        when(jpaRepositoryMock
            .findById(userId.value()))
            .thenReturn(Optional.empty());

        // when
        Optional<User> result = repository.findById(userId);

        // then
        assertTrue(result.isEmpty());
        verify(jpaRepositoryMock).findById(userId.value());
        verifyNoMoreInteractions(jpaRepositoryMock);
        verifyNoInteractions(converterMock);
    }

    @Test
    void findByEmailWhenUserExistsReturnsUser() {
        // given
        Email email = Email.of("test@example.com");
        UserEntity userEntity = mock(UserEntity.class);
        User expectedUser = mock(User.class);

        when(jpaRepositoryMock
            .findByEmail(email.value()))
            .thenReturn(Optional.of(userEntity));

        when(converterMock
            .convert(eq(userEntity), eq(User.class)))
            .thenReturn(expectedUser);

        // when
        Optional<User> result = repository.findByEmail(email);

        // then
        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());
        verify(jpaRepositoryMock).findByEmail(email.value());
        verify(converterMock).convert(userEntity, User.class);
        verifyNoMoreInteractions(jpaRepositoryMock, converterMock);
    }

    @Test
    void findByEmailWhenUserDoesNotExistReturnsEmptyOptional() {
        // given
        Email email = Email.of("test@example.com");
        when(jpaRepositoryMock
            .findByEmail(any()))
            .thenReturn(Optional.empty());

        // when
        Optional<User> result = repository.findByEmail(email);

        // then
        assertTrue(result.isEmpty());
        verify(jpaRepositoryMock).findByEmail(email.value());
        verifyNoMoreInteractions(jpaRepositoryMock);
        verifyNoInteractions(converterMock);
    }

    @Test
    void findAllWhenUsersExistReturnsPageWithUsers() {
        // given
        Pageable pageRequest = mock(Pageable.class);

        UserEntity userEntity = mock(UserEntity.class);
        User expectedUser = mock(User.class);

        Page<UserEntity> usersEntitiesPage = new PageImpl<>(List.of(userEntity));

        when(jpaRepositoryMock
            .findAll(pageRequest))
            .thenReturn(usersEntitiesPage);

        when(converterMock
            .convert(eq(userEntity), eq(User.class)))
            .thenReturn(expectedUser);

        // when
        Page<User> usersPage = repository.findAll(pageRequest);

        // then
        assertNotNull(usersPage);
        assertThat(usersPage).containsExactly(expectedUser);
        verify(jpaRepositoryMock).findAll(pageRequest);
        verifyNoMoreInteractions(jpaRepositoryMock);
    }

    @Test
    void findAllWhenNoUsersExistReturnsEmptyPage() {
        // given
        Pageable pageRequest = mock(Pageable.class);
        Page<UserEntity> emptyPage = Page.empty();
        when(jpaRepositoryMock
            .findAll(pageRequest))
            .thenReturn(emptyPage);

        // when
        Page<User> result = repository.findAll(pageRequest);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jpaRepositoryMock).findAll(pageRequest);
        verifyNoMoreInteractions(jpaRepositoryMock);
        verifyNoInteractions(converterMock);
    }

    @Test
    void saveWhenValidUserThenReturnsSavedUser() {
        // given
        User userToSave = mock(User.class);
        UserEntity entityToSave = mock(UserEntity.class);
        UserEntity savedEntity = mock(UserEntity.class);
        User expectedUser = mock(User.class);

        when(jpaRepositoryMock
            .save(entityToSave))
            .thenReturn(savedEntity);

        when(converterMock
            .convert(eq(userToSave), eq(UserEntity.class)))
            .thenReturn(entityToSave);

        when(converterMock
            .convert(eq(savedEntity), eq(User.class)))
            .thenReturn(expectedUser);

        // when
        User result = repository.save(userToSave);

        // then
        assertNotNull(result);
        assertEquals(expectedUser, result);
        verify(converterMock).convert(userToSave, UserEntity.class);
        verify(jpaRepositoryMock).save(entityToSave);
        verify(converterMock).convert(savedEntity, User.class);
        verifyNoMoreInteractions(jpaRepositoryMock, converterMock);
    }

    @Test
    void saveWhenNullUserThenThrowsException() {
        // when/then
        assertThrows(IllegalArgumentException.class, () -> repository.save(null));
        verifyNoInteractions(jpaRepositoryMock, converterMock);
    }

    @Test
    void deleteWhenValidUserThenDeletesUser() {
        // given
        User userToDelete = mock(User.class);
        UserEntity entityToDelete = mock(UserEntity.class);

        when(converterMock
            .convert(eq(userToDelete), eq(UserEntity.class)))
            .thenReturn(entityToDelete);

        // when
        repository.delete(userToDelete);

        // then
        verify(converterMock).convert(userToDelete, UserEntity.class);
        verify(jpaRepositoryMock).delete(entityToDelete);
        verifyNoMoreInteractions(jpaRepositoryMock, converterMock);
    }

    @Test
    void deleteWhenNullUserThenThrowsException() {
        // when/then
        assertThrows(IllegalArgumentException.class, () -> repository.delete(null));
        verifyNoInteractions(jpaRepositoryMock, converterMock);
    }
}