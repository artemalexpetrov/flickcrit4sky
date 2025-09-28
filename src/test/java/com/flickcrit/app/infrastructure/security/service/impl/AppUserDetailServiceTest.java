package com.flickcrit.app.infrastructure.security.service.impl;

import com.flickcrit.app.domain.exception.EntityNotFoundException;
import com.flickcrit.app.domain.model.user.Email;
import com.flickcrit.app.domain.model.user.User;
import com.flickcrit.app.domain.model.user.UserId;
import com.flickcrit.app.domain.model.user.UserRole;
import com.flickcrit.app.domain.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserDetailServiceTest {

    @Mock
    private UserService userServiceMock;

    @InjectMocks
    private AppUserDetailService userDetailService;

    @Test
    void givenUserWhenLoadUserByUsernameExpectUserDetails() {
        // given
        User user = buildUser();
        String expectedAuthority = "ROLE_USER";

        when(userServiceMock
            .getUser(any(Email.class)))
            .thenReturn(user);

        // when
        UserDetails userDetails = userDetailService.loadUserByUsername(user.getEmail().value());

        // then
        assertNotNull(userDetails);
        assertEquals(user.getEmail().value(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertThat(userDetails.getAuthorities())
            .extracting(GrantedAuthority::getAuthority)
            .containsExactlyInAnyOrder(expectedAuthority);
        verify(userServiceMock).getUser(user.getEmail());
        verifyNoMoreInteractions(userServiceMock);
    }

    @Test
    void givenNonExistentUserWhenLoadUserByUsernameExpectUsernameNotFoundException() {
        // given
        String email = "nonexistent@example.com";
        when(userServiceMock
            .getUser(any(Email.class)))
            .thenThrow(new EntityNotFoundException("User not found"));

        // when & then
        assertThrows(UsernameNotFoundException.class, () -> userDetailService.loadUserByUsername(email));
        verify(userServiceMock).getUser(Email.of(email));
        verifyNoMoreInteractions(userServiceMock);
    }


    private static User buildUser() {
        return User.builder()
            .id(UserId.of(15L))
            .email(Email.of("test@example.com"))
            .password("encryptedPassword")
            .roles(Set.of(UserRole.USER))
            .build();
    }
}