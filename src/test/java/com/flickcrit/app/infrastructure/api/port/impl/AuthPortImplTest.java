package com.flickcrit.app.infrastructure.api.port.impl;

import com.flickcrit.app.domain.model.user.Email;
import com.flickcrit.app.domain.service.UserService;
import com.flickcrit.app.infrastructure.api.model.auth.RefreshTokenRequestDto;
import com.flickcrit.app.infrastructure.api.model.auth.SignInRequestDto;
import com.flickcrit.app.infrastructure.api.model.auth.SignUpRequestDto;
import com.flickcrit.app.infrastructure.security.service.AuthService;
import com.flickcrit.app.infrastructure.security.service.TokenPair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthPortImplTest {

    @Mock
    private AuthService authServiceMock;

    @Mock
    private UserService userServiceMock;

    @InjectMocks
    private AuthPortImpl authPort;

    @Test
    void givenValidRequestWhenSignUpExpectAuthResponse() {
        // given
        String email = "test@example.com";
        String password = "password123";

        SignUpRequestDto request = new SignUpRequestDto(email, password, password);
        TokenPair tokenPair = mock(TokenPair.class);
        String encodedPassword = "encodedPassword";

        when(authServiceMock
            .encodePassword(any()))
            .thenReturn(encodedPassword);

        when(authServiceMock
            .authenticate(any(), any()))
            .thenReturn(tokenPair);

        // when
        authPort.signUp(request);

        // then
        verify(userServiceMock).createUser(Email.of(email), encodedPassword);
        verify(authServiceMock).encodePassword(password);
        verifyNoMoreInteractions(userServiceMock, authServiceMock);
    }

    @Test
    void givenValidCredentialsWhenSignInExpectAuthResponse() {
        // given
        String email = "test@example.com";
        String password = "password123";
        SignInRequestDto request = new SignInRequestDto(email, password);
        TokenPair expectedTokenPair = mock(TokenPair.class);

        when(authServiceMock
            .authenticate(any(), any()))
            .thenReturn(expectedTokenPair);

        // when
        TokenPair tokenPair = authPort.signIn(request);

        // then
        assertEquals(expectedTokenPair, tokenPair);
        verify(authServiceMock).authenticate(request.email(), request.password());
        verifyNoMoreInteractions(authServiceMock);
        verifyNoInteractions(userServiceMock);
    }

    @Test
    void givenValidRefreshTokenWhenRefreshTokenExpectNewTokenPair() {
        // given
        String refreshToken = "valid_refresh_token";
        RefreshTokenRequestDto request = new RefreshTokenRequestDto(refreshToken);
        TokenPair expectedTokenPair = mock(TokenPair.class);

        when(authServiceMock
            .refreshToken(any()))
            .thenReturn(expectedTokenPair);

        // when
        TokenPair tokenPair = authPort.refreshToken(request);

        // then
        assertEquals(expectedTokenPair, tokenPair);
        verify(authServiceMock).refreshToken(request.refreshToken());
        verifyNoMoreInteractions(authServiceMock);
        verifyNoInteractions(userServiceMock);
    }
}