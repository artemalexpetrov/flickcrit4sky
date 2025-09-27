package com.flickcrit.app.infrastructure.api.port;

import com.flickcrit.app.domain.model.user.UserId;
import com.flickcrit.app.infrastructure.api.model.common.PageResponse;
import com.flickcrit.app.infrastructure.api.model.user.UserDto;
import org.springframework.data.domain.Pageable;

public interface UserPort {

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique identifier of the user to retrieve
     * @return the details of the user as a UserDto
     */
    UserDto getUser(UserId id);

    /**
     * Retrieves a paginated and sorted list of users.
     *
     * @param pagingRequest the pagination and sorting details
     * @return a page containing a list of UserDto objects
     */
    PageResponse<UserDto> getUsers(Pageable pagingRequest);

    /**
     * Deletes a user identified by the provided unique identifier.
     *
     * @param id the unique identifier of the user to delete
     */
    void deleteUser(UserId id);
}
