package com.flickcrit.app.infrastructure.security.service.impl;

import com.flickcrit.app.infrastructure.security.service.Token;
import com.flickcrit.app.infrastructure.security.service.TokenPair;
import com.flickcrit.app.infrastructure.security.service.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private TokenService tokenServiceMock;

    @Mock
    private PasswordEncoder passwordEncoderMock;

    @Mock
    private UserDetailsService userDetailsServiceMock;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void givenValidCredentialsWhenAuthenticateExpectTokenPair() {
        // given
        String username = "test@example.com";
        String password = "password";
        UserDetails userDetails = mock(UserDetails.class);
        Authentication authentication = mock(Authentication.class);
        TokenPair expectedTokenPair = mock(TokenPair.class);

        when(authentication
            .getPrincipal())
            .thenReturn(userDetails);

        when(authenticationManager
            .authenticate(any()))
            .thenReturn(authentication);

        when(tokenServiceMock
            .issueToken(userDetails))
            .thenReturn(expectedTokenPair);

        // when
        TokenPair tokenPair = authService.authenticate(username, password);

        // then
        assertEquals(expectedTokenPair, tokenPair);
        verify(authenticationManager).authenticate(assertArg(userPassToken -> {
            assertEquals(username, userPassToken.getName());
            assertEquals(password, userPassToken.getCredentials());
        }));

        verify(tokenServiceMock).issueToken(userDetails);
        verifyNoMoreInteractions(authenticationManager, tokenServiceMock);
    }

    @Test
    void givenInvalidCredentialsWhenAuthenticateExpectBadCredentialsException() {
        // given
        String username = "test@example.com";
        String password = "wrong_password";

        when(authenticationManager
            .authenticate(any()))
            .thenThrow(new BadCredentialsException("Bad credentials"));

        // when & then
        assertThrows(BadCredentialsException.class, () -> authService.authenticate(username, password));
        verify(authenticationManager).authenticate(any());
        verifyNoMoreInteractions(authenticationManager);
        verifyNoInteractions(tokenServiceMock);
    }

    @Test
    void givenValidRefreshTokenWhenRefreshTokenExpectNewTokenPair() {
        // given
        String refreshToken = "valid_refresh_token";
        String username = "test@example.com";
        Token token = mock(Token.class);
        UserDetails userDetails = mock(UserDetails.class);
        TokenPair expectedTokenPair = mock(TokenPair.class);

        when(token.isExpired())
            .thenReturn(false);

        when(token
            .getUsername())
            .thenReturn(username);

        when(tokenServiceMock
            .parseToken(any()))
            .thenReturn(token);

        when(userDetailsServiceMock
            .loadUserByUsername(any()))
            .thenReturn(userDetails);

        when(tokenServiceMock
            .issueToken(any()))
            .thenReturn(expectedTokenPair);

        // when
        TokenPair actualTokenPair = authService.refreshToken(refreshToken);

        // then
        assertEquals(expectedTokenPair, actualTokenPair);
        verify(tokenServiceMock).parseToken(refreshToken);
        verify(userDetailsServiceMock).loadUserByUsername(username);
        verify(tokenServiceMock).issueToken(userDetails);
        verifyNoMoreInteractions(tokenServiceMock, userDetailsServiceMock);
    }

    @Test
    void givenInvalidRefreshTokenWhenRefreshTokenExpectBadCredentialsException() {
        // given
        String refreshToken = "invalid_refresh_token";

        when(tokenServiceMock
            .parseToken(refreshToken))
            .thenReturn(null);

        // when & then
        assertThrows(BadCredentialsException.class, () -> authService.refreshToken(refreshToken));
        verify(tokenServiceMock).parseToken(refreshToken);
        verifyNoMoreInteractions(tokenServiceMock);
        verifyNoInteractions(userDetailsServiceMock);
    }

    @Test
    void givenExpiredRefreshTokenWhenRefreshTokenExpectBadCredentialsException() {
        // given
        String refreshToken = "expired_refresh_token";
        Token token = mock(Token.class);

        when(token
            .isExpired())
            .thenReturn(true);

        when(tokenServiceMock
            .parseToken(any()))
            .thenReturn(token);

        // when & then
        assertThrows(BadCredentialsException.class, () -> authService.refreshToken(refreshToken));
        verify(tokenServiceMock).parseToken(refreshToken);
        verifyNoMoreInteractions(tokenServiceMock);
        verifyNoInteractions(userDetailsServiceMock);
    }

    @Test
    void givenRawPasswordWhenEncodePasswordExpectEncodedPassword() {
        // given
        String rawPassword = "rawPassword";
        String expectedEncodedPassword = "encodedPassword";

        when(passwordEncoderMock
            .encode(any()))
            .thenReturn(expectedEncodedPassword);

        // when
        String encodedPassword = authService.encodePassword(rawPassword);

        // then
        assertEquals(expectedEncodedPassword, encodedPassword);
        verify(passwordEncoderMock).encode(rawPassword);
        verifyNoMoreInteractions(passwordEncoderMock);
    }

    @ParameterizedTest
    @CsvSource(value = {"''", "'  '", "NULL"}, nullValues = "NULL")
    void givenInvalidRawPasswordWhenEncodePasswordExpectException(String invalidPassword) {
        assertThrows(IllegalArgumentException.class, () -> authService.encodePassword(invalidPassword));
        verifyNoInteractions(passwordEncoderMock);
    }
}