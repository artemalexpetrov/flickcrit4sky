package com.flickcrit.app.infrastructure.persistence.model;

import org.springframework.data.domain.Persistable;

interface PersistableEntity extends Persistable<Long> {

    @Override
    default boolean isNew() {
        return getId() == null;
    }
}
