package com.flickcrit.app.infrastructure.api.controller;

import com.flickcrit.app.domain.model.user.UserId;
import com.flickcrit.app.infrastructure.api.model.common.PageRequestDto;
import com.flickcrit.app.infrastructure.api.model.common.PageResponse;
import com.flickcrit.app.infrastructure.api.model.user.UserDto;
import com.flickcrit.app.infrastructure.api.port.UserPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static java.util.Objects.requireNonNullElse;


@RestController
@Secured("ROLE_ADMIN")
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Users", description = "User management endpoints")
public class UserControllerV1 {

    private final UserPort usersPort;

    @Operation(
        summary = "Get all users",
        description = "Returns a paginated list of users. Requires ADMIN role."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved users")
    @ApiResponse(responseCode = "403", description = "Access denied")
    @GetMapping
    PageResponse<UserDto> getUsers(
        @Parameter(description = "Pagination parameters") @Valid PageRequestDto pageRequest) {
        return usersPort.getUsers(requireNonNullElse(pageRequest, PageRequestDto.defaultRequest()));
    }

    @Operation(
        summary = "Get user by ID",
        description = "Returns a single user by their ID. Requires ADMIN role."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "403", description = "Access denied")
    @GetMapping("/{id:\\d+}")
    UserDto getUser(@Parameter(description = "User ID") @PathVariable UserId id) {
        return usersPort.getUser(id);
    }

    @Operation(
        summary = "Delete user",
        description = "Deletes a user by their ID. Requires ADMIN role."
    )
    @ApiResponse(responseCode = "204", description = "User successfully deleted")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "403", description = "Access denied")
    @DeleteMapping("/{id:\\d+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@Parameter(description = "User ID") @PathVariable UserId id) {
        usersPort.deleteUser(id);
    }
}
