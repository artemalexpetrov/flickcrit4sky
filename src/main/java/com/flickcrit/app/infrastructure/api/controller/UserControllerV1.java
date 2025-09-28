package com.flickcrit.app.infrastructure.api.controller;

import com.flickcrit.app.domain.model.user.UserId;
import com.flickcrit.app.infrastructure.api.model.common.PageResponse;
import com.flickcrit.app.infrastructure.api.model.user.UserDto;
import com.flickcrit.app.infrastructure.api.port.UserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@Secured("ROLE_ADMIN")
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserControllerV1 {

    private final UserPort usersPort;

    @GetMapping
    PageResponse<UserDto> getUsers(Pageable pageRequest) {
        return usersPort.getUsers(pageRequest);
    }

    @GetMapping("/{id:\\d+}")
    UserDto getUser(@PathVariable UserId id) {
        return usersPort.getUser(id);
    }

    @DeleteMapping("/{id:\\d+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable UserId id) {
        usersPort.deleteUser(id);
    }
}
