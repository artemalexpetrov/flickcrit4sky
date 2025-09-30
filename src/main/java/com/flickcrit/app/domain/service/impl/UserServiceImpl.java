package com.flickcrit.app.domain.service.impl;

import com.flickcrit.app.domain.exception.EntityNotFoundException;
import com.flickcrit.app.domain.model.user.Email;
import com.flickcrit.app.domain.model.user.User;
import com.flickcrit.app.domain.model.user.UserId;
import com.flickcrit.app.domain.repository.UserRepository;
import com.flickcrit.app.domain.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    @Transactional
    public User createUser(Email email, String password) {
        log.info("Creating a new user with email {}", email);
        User user = new User(email, password);
        return repository.save(user);
    }

    @Override
    public User getUser(@NonNull UserId id) {
        return repository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public User getUser(@NonNull Email email) {
        return repository
            .findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public Page<User> getUsers(@NonNull Pageable pagingRequest) {
        return repository.findAll(pagingRequest);
    }

    @Override
    @Transactional
    public User save(@NonNull User user) {
        return repository.save(user);
    }

    @Override
    @Transactional
    public void delete(@NonNull User user) {
        log.info("Deleting a user with ID {}", user.getId());
        repository.delete(user);
    }
}
