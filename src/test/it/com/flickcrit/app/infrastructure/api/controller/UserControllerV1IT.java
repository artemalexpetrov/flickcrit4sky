package com.flickcrit.app.infrastructure.api.controller;

import com.flickcrit.app.domain.exception.EntityNotFoundException;
import com.flickcrit.app.domain.model.user.UserId;
import com.flickcrit.app.domain.model.user.UserRole;
import com.flickcrit.app.infrastructure.api.model.common.PageRequestDto;
import com.flickcrit.app.infrastructure.api.model.common.PageResponse;
import com.flickcrit.app.infrastructure.api.model.user.UserDto;
import com.flickcrit.app.infrastructure.api.port.UserPort;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(roles = "ADMIN")
@WebMvcTest(UserControllerV1.class)
public class UserControllerV1IT extends BaseControllerIT {

    @MockitoBean
    private UserPort portMock;

    @Test
    void whenGetUsersExpectUsersPage() throws Exception {
        // given
        PageRequestDto pageRequest = PageRequestDto.of(10, 40);
        UserDto userDto = createUserBuilder().build();

        when(portMock
            .getUsers(any()))
            .thenReturn(PageResponse.of(List.of(userDto)));

        // when / then
        mockMvc.perform(get("/api/v1/users?page=10&size=40")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.items").isArray())
            .andExpect(jsonPath("$.items[0].id").value(userDto.id()))
            .andExpect(jsonPath("$.items[0].email").value(userDto.email()))
            .andExpect(jsonPath("$.items[0].roles").isArray())
            .andExpect(jsonPath("$.items[0].roles[0]").value(UserRole.USER.name()));

        verify(portMock).getUsers(pageRequest);
        verifyNoMoreInteractions(portMock);
    }

    @Test
    void givenInvalidPageNumberWhenGetUsersExpectBadRequest() throws Exception {
        // when / then
        mockMvc.perform(get("/api/v1/users?page=-1&size=10")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));

        verifyNoInteractions(portMock);
    }

    @Test
    void givenInvalidPageSizeWhenGetUsersExpectBadRequest() throws Exception {
        // when / then
        mockMvc.perform(get("/api/v1/users?page=1&size=1000")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));

        verifyNoInteractions(portMock);
    }

    @Test
    void givenValidIdWhenGetUserThenReturnUser() throws Exception {
        // given
        UserDto userDto = createUserBuilder().build();
        UserId userId = UserId.of(userDto.id());

        when(portMock
            .getUser(any(UserId.class)))
            .thenReturn(userDto);

        // when / then
        mockMvc.perform(get("/api/v1/users/" + userId.value())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userDto.id()))
            .andExpect(jsonPath("$.email").value(userDto.email()))
            .andExpect(jsonPath("$.roles[0]").value(UserRole.USER.name()));

        verify(portMock).getUser(userId);
        verifyNoMoreInteractions(portMock);
    }

    @Test
    void givenInvalidIdWhenGetUserThenReturn404() throws Exception {
        // given
        UserId userId = UserId.of(15L);

        when(portMock
            .getUser(any(UserId.class)))
            .thenThrow(new EntityNotFoundException("User not found"));

        // when / then
        mockMvc.perform(get("/api/v1/users/{id}", userId.value())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));

        verify(portMock).getUser(userId);
        verifyNoMoreInteractions(portMock);
    }

    @Test
    void givenValidIdWhenDeleteUserThenReturnNoContent() throws Exception {
        // given
        UserId userId = UserId.of(15L);

        // when / then
        mockMvc
            .perform(delete("/api/v1/users/{id}", userId.value()))
            .andExpect(status().isNoContent());

        verify(portMock).deleteUser(userId);
        verifyNoMoreInteractions(portMock);
    }

    @Test
    void givenNonExistingIdWhenDeleteMovieThenReturn404() throws Exception {
        // given
        UserId userId = UserId.of(15L);

        doThrow(new EntityNotFoundException("Movie not found"))
            .when(portMock).deleteUser(any(UserId.class));

        // when / then
        mockMvc.perform(delete("/api/v1/users/{id}", userId.value()))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));

        verify(portMock).deleteUser(userId);
        verifyNoMoreInteractions(portMock);
    }

    private UserDto.UserDtoBuilder createUserBuilder() {
        return UserDto.builder()
            .id(199L)
            .email("test@example.com")
            .roles(Set.of(UserRole.USER));
    }
}
