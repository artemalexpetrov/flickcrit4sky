package com.flickcrit.app.infrastructure.core.conversion.mapper;

import com.flickcrit.app.domain.model.common.EntityId;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.mapstruct.TargetType;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.util.Set;

import static java.util.Objects.isNull;

/**
 * Utility class for mapping between EntityID objects and their underlying values.
 * This class provides methods to retrieve the raw value from an EntityID and
 * to construct a new EntityID instance from a given value. It is typically used
 * to bridge entity ID representations between domain models and DTOs.
 */
@Component
@SuppressWarnings({"rawtypes", "unchecked"})
public class EntityIdMapper implements GenericConverter {

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Set.of(
            new ConvertiblePair(EntityId.class, Object.class),
            new ConvertiblePair(Object.class, EntityId.class)
        );
    }

    @Override
    public Object convert(
        Object source,
        @Nonnull TypeDescriptor sourceType,
        @Nonnull TypeDescriptor targetType) {

        if (source == null) {
            return null;
        }

        if (source instanceof EntityId entityId) {
            return mapToValue(entityId);
        }

        return doMapToId(source, targetType.getType());
    }

    /**
     * Maps the provided {@link EntityId} to its underlying value.
     *
     * @param id the {@link EntityId} to map; may be null
     * @return the value contained within the {@link EntityId}, or null if the provided id is null
     */
    public <T, ID extends EntityId<T>> @Nullable T mapToValue(ID id) {
        return isNull(id) ? null : id.value();
    }

    /**
     * Converts the given value to an instance of the specified EntityID type.
     * If the input value is null, returns null.
     *
     * @param <T>   the type of the value being converted
     * @param <ID>  the type of the EntityID to be returned
     * @param value the value to be converted into an EntityID; may be null
     * @param type  the class type of the EntityID; must not be null
     * @return an instance of the specified EntityID type containing the given value, or null if the value is null
     * @throws ConversionFailedException if the conversion fails due to reflection issues or mismatch
     */
    public <T, ID extends EntityId<T>> @Nullable ID mapToId(T value, @TargetType @Nonnull Class<ID> type) {
        if (isNull(value)) {
            return null;
        }

        return (ID) doMapToId(value, type);
    }

    private Object doMapToId(Object value, Class<?> targetType) {
        try {
            Constructor<?> constructor = targetType.getDeclaredConstructor(value.getClass());
            constructor.setAccessible(true);
            return constructor.newInstance(value);

        } catch (Exception e) {
            throw new ConversionFailedException(
                TypeDescriptor.valueOf(value.getClass()),
                TypeDescriptor.valueOf(targetType),
                value,
                e);
        }
    }
}
