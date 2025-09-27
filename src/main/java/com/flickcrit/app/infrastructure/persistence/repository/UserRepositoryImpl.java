package com.flickcrit.app.infrastructure.persistence.repository;

import com.flickcrit.app.domain.model.user.Email;
import com.flickcrit.app.domain.model.user.User;
import com.flickcrit.app.domain.model.user.UserId;
import com.flickcrit.app.domain.repository.UserRepository;
import com.flickcrit.app.infrastructure.persistence.model.UserEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaRepository;
    private final ConversionService converter;

    @Override
    public Optional<User> findById(@NonNull UserId id) {
        return jpaRepository
            .findById(id.value())
            .map(this::convertToDomain);
    }

    @Override
    public Optional<User> findByEmail(@NonNull Email email) {
        return jpaRepository
            .findByEmail(email.value())
            .map(this::convertToDomain);
    }

    @Override
    public Page<User> findAll(@NonNull Pageable pageRequest) {
        return jpaRepository
            .findAll(pageRequest)
            .map(this::convertToDomain);
    }

    @Override
    public User save(@NonNull User user) {
        UserEntity entityToSave = convertToEntity(user);
        UserEntity savedUser = jpaRepository.save(entityToSave);
        return convertToDomain(savedUser);
    }

    @Override
    public void delete(@NonNull User user) {
        jpaRepository.delete(convertToEntity(user));
    }

    private User convertToDomain(UserEntity entity) {
        return converter.convert(entity, User.class);
    }

    private UserEntity convertToEntity(User user) {
        return converter.convert(user, UserEntity.class);
    }
}
