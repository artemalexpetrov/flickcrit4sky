package com.flickcrit.app.infrastructure.persistence.repository;

import com.flickcrit.app.BaseSpringBootIT;
import com.flickcrit.app.domain.model.user.UserRole;
import com.flickcrit.app.infrastructure.persistence.model.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class JpaUserRepositoryIT extends BaseSpringBootIT {

    @Autowired
    private JpaUserRepository repository;

    @Test
    void givenUserWhenFindByEmailExpectUser() {
        // given
        String email = "test@example.com";
        repository.save(UserEntity.builder()
            .email(email)
            .password("password123")
            .roles(new UserRole[]{UserRole.ADMIN})
            .build());

        // when
        Optional<UserEntity> foundUser = repository.findByEmail(email);

        // then
        assertThat(foundUser).isPresent();
    }

    @Test
    void givenNoUsersWhenFindByEmailExpectEmptyResult() {
        // when
        Optional<UserEntity> foundUser = repository.findByEmail("test@email.com");

        // then
        assertThat(foundUser).isEmpty();
    }
}
