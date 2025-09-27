package com.flickcrit.app.infrastructure.persistence.repository;

import com.flickcrit.app.infrastructure.persistence.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface JpaUserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Retrieves a UserEntity based on the provided email.
     *
     * @param email the email address of the user to be retrieved; must not be null.
     * @return the UserEntity corresponding to the given email
     */
    Optional<UserEntity> findByEmail(String email);
}
