package com.flickcrit.app.domain.model.common;

import com.flickcrit.app.utils.ObjectUtils;

import java.util.Objects;

/**
 * Represents an abstract identifier for an entity. This class is designed to encapsulate
 * the unique identifier of a specific type of entity in a strongly-typed manner.
 *
 * @param <T> the type of the underlying value used for the entity identifier
 */
public abstract class EntityId<T>{

    private final T value;

    protected EntityId(T value) {
        this.value = ObjectUtils.requireNonNull(value, "Entity ID value cannot be null");
    }

    public T value() {
        return value;
    }

    @Override
    public final String toString() {
        return "%s[%s]".formatted(getClass().getSimpleName(), value);
    }

    @Override
    public final boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EntityId<?> entityId = (EntityId<?>) o;
        return Objects.equals(value, entityId.value);
    }

    @Override
    public final int hashCode() {
        return 31 * getClass().hashCode() + value.hashCode();
    }
}
