package com.flickcrit.app.domain.service.impl;

import com.flickcrit.app.domain.exception.EntityNotFoundException;
import com.flickcrit.app.domain.model.user.Email;
import com.flickcrit.app.domain.model.user.User;
import com.flickcrit.app.domain.model.user.UserId;
import com.flickcrit.app.domain.repository.UserRepository;
import com.flickcrit.app.domain.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public User createUser(Email email, String password) {
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
    public User save(@NonNull User user) {
        return repository.save(user);
    }

    @Override
    public void delete(@NonNull User user) {
        repository.delete(user);
    }
}
