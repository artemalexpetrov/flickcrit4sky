package com.flickcrit.app.domain.repository;

import com.flickcrit.app.domain.model.user.Email;
import com.flickcrit.app.domain.model.user.User;
import com.flickcrit.app.domain.model.user.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Interface for managing User entities in a data repository.
 * Provides methods for CRUD operations and retrieval of users.
 */
public interface UserRepository {
    
    /**
     * Retrieves a user by its unique identifier.
     *
     * @param id the unique identifier of the user to find; must not be null
     * @return an {@code Optional} containing the found user,
     * or an empty {@code Optional} if no user exists with the given identifier
     */
    Optional<User> findById(UserId id);

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address of the user to find; must not be null
     * @return an {@code Optional} containing the found user,
     * or an empty {@code Optional} if no user exists with the given email
     */
    Optional<User> findByEmail(Email email);

    /**
     * Retrieves a paginated list of all User entities.
     *
     * @param pageRequest the pagination and sorting information; must not be null
     * @return a Page containing the User entities for the specified pagination
     */
    Page<User> findAll(Pageable pageRequest);

    /**
     * Saves the provided User entity to the data repository.
     * If the user already exists, it updates the existing entry;
     * otherwise, a new entry is created.
     *
     * @param user the User entity to save; must not be null
     * @return the saved User entity
     */
    User save(User user);

    /**
     * Deletes the specified user from the repository.
     *
     * @param user the user entity to be deleted; must not be null
     */
    void delete(User user);
}
