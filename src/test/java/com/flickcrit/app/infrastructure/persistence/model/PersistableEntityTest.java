package com.flickcrit.app.infrastructure.persistence.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PersistableEntityTest {

    @Test
    void givenEntityWithNullIdWhenIsNewExpectTrue() {
        DummyEntity entity = new DummyEntity(null);
        assertThat(entity.isNew()).isTrue();
    }

    @Test
    void givenEntityWithNonNullIdWhenIsNewExpectTrue() {
        DummyEntity entity = new DummyEntity(null);
        assertThat(entity.isNew()).isTrue();
    }

    @Getter
    @RequiredArgsConstructor
    private static class DummyEntity implements PersistableEntity {
        private final Long id;
    }
}