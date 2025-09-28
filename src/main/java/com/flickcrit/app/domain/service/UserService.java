package com.flickcrit.app.domain.service;

import com.flickcrit.app.domain.model.user.Email;
import com.flickcrit.app.domain.model.user.User;
import com.flickcrit.app.domain.model.user.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing users in the application.
 * Provides methods for creating, retrieving, updating, and deleting user entities,
 * as well as handling pagination and user identification functionalities.
 */
public interface UserService {

    /**
     * Creates a new user with the specified email and password.
     * The created user will have a unique identifier and the provided credentials.
     *
     * @param email the email address of the user to be created; must not be null
     * @param password the password of the user to be created; must not be null
     * @return the newly created user
     */
    User createUser(Email email, String password);

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique identifier of the user to retrieve, must not be null
     * @return the user associated with the given identifier
     */
    User getUser(UserId id);

    /**
     * Retrieves a user associated with the given email.
     *
     * @param email the email of the user to retrieve; must not be null
     * @return the user associated with the specified email
     */
    User getUser(Email email);

    /**
     * Retrieves a paginated list of users based on the given page request.
     *
     * @param pagingRequest the page request containing pagination and sorting details, must not be null
     * @return a page containing a list of users
     */
    Page<User> getUsers(Pageable pagingRequest);

    /**
     * Persists the provided user entity in the system. If the user already exists, updates the existing record.
     *
     * @param user the user entity to save, must not be null
     * @return the saved user entity with potentially updated information
     */
    User save(User user);

    /**
     * Deletes the specified user from the system.
     *
     * @param user the user to delete, must not be null
     */
    void delete(User user);
}
