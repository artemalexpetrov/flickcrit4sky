package com.flickcrit.app.infrastructure.api.controller;

import com.flickcrit.app.domain.exception.UsernameConflictException;
import com.flickcrit.app.infrastructure.api.model.auth.RefreshTokenRequestDto;
import com.flickcrit.app.infrastructure.api.model.auth.SignInRequestDto;
import com.flickcrit.app.infrastructure.api.model.auth.SignUpRequestDto;
import com.flickcrit.app.infrastructure.api.port.AuthPort;
import com.flickcrit.app.infrastructure.security.service.TokenPair;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthControllerV1.class)
public class AuthControllerV1IT extends BaseControllerIT {

    @MockitoBean
    private AuthPort portMock;

    @Test
    void givenSignUpRequestWhenSignUpExpectCreated() throws Exception {
        // given
        SignUpRequestDto request = new SignUpRequestDto("test@example.com", "password", "password");

        // when / then
        mockMvc
            .perform(post("/api/v1/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());

        verify(portMock).signUp(request);
        verifyNoMoreInteractions(portMock);
    }

    @Test
    void givenSignUpRequestWhenUserExistExpectConflict() throws Exception {
        // given
        SignUpRequestDto request = new SignUpRequestDto("test@example.com", "password", "password");

        doThrow(new UsernameConflictException("user"))
            .when(portMock)
            .signUp(any());

        // when / then
        mockMvc
            .perform(post("/api/v1/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict());

        verify(portMock).signUp(request);
        verifyNoMoreInteractions(portMock);
    }

    @Test
    void givenInvalidSignUpRequestWheSignUpExpectBadRequest() throws Exception {
        // given
        SignUpRequestDto request = new SignUpRequestDto("test", "pass", "differentPass");

        // when / then
        mockMvc
            .perform(post("/api/v1/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verifyNoInteractions(portMock);
    }

    @Test
    void givenSignInRequestWhenSignInExpectOk() throws Exception {
        // given
        SignInRequestDto request = new SignInRequestDto("test@example.com", "password");
        TokenPair expectedResponse = new TokenPair("access-token", "refresh-token");

        when(portMock
            .signIn(request))
            .thenReturn(expectedResponse);

        // when / then
        mockMvc
            .perform(post("/api/v1/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").value("access-token"))
            .andExpect(jsonPath("$.refreshToken").value("refresh-token"));

        verify(portMock).signIn(request);
        verifyNoMoreInteractions(portMock);
    }

    @Test
    void givenInvalidSignInRequestWhenSignInExpectBadRequest() throws Exception {
        // given
        SignInRequestDto request = new SignInRequestDto("invalid-email", "");

        // when / then
        mockMvc
            .perform(post("/api/v1/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verifyNoInteractions(portMock);
    }

    @Test
    void givenRefreshTokenRequestWhenRefreshTokenExpectOk() throws Exception {
        // given
        RefreshTokenRequestDto request = new RefreshTokenRequestDto("refresh-token");
        TokenPair expectedResponse = new TokenPair("new-access-token", "new-refresh-token");

        when(portMock.refreshToken(request))
            .thenReturn(expectedResponse);

        // when / then
        mockMvc
            .perform(post("/api/v1/auth/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").value("new-access-token"))
            .andExpect(jsonPath("$.refreshToken").value("new-refresh-token"));

        verify(portMock).refreshToken(request);
        verifyNoMoreInteractions(portMock);
    }

    @Test
    void givenInvalidRefreshTokenRequestWhenRefreshTokenExpectBadRequest() throws Exception {
        // given
        RefreshTokenRequestDto request = new RefreshTokenRequestDto("");

        // when / then
        mockMvc
            .perform(post("/api/v1/auth/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verifyNoInteractions(portMock);
    }
}
