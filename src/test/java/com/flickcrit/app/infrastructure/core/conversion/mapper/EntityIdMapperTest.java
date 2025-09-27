package com.flickcrit.app.infrastructure.core.conversion.mapper;

import com.flickcrit.app.domain.model.common.EntityId;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class EntityIdMapperTest {

    private final EntityIdMapper mapper = new EntityIdMapper();

    @Test
    void whenGetConvertibleTypesExpectOnlySupportedTypes() {
        // given
        Set<GenericConverter.ConvertiblePair> expectedTypes = Set.of(
            new GenericConverter.ConvertiblePair(EntityId.class, Object.class),
            new GenericConverter.ConvertiblePair(Object.class, EntityId.class)
        );

        // when / then
        assertThat(mapper.getConvertibleTypes()).containsExactlyInAnyOrderElementsOf(expectedTypes);
    }

    @Test
    void whenConvertingEntityIdThenReturnValue() {
        // given
        TestId id = new TestId(123L);
        TypeDescriptor sourceType = TypeDescriptor.valueOf(TestId.class);
        TypeDescriptor targetType = TypeDescriptor.valueOf(Long.class);

        // when
        Object result = mapper.convert(id, sourceType, targetType);

        // then
        assertThat(result).isEqualTo(id.value());
    }

    @Test
    void whenConvertingNullEntityIdThenReturnNull() {
        // given
        TestId id = null;
        TypeDescriptor sourceType = TypeDescriptor.valueOf(TestId.class);
        TypeDescriptor targetType = TypeDescriptor.valueOf(Long.class);

        // when / then
        assertNull(mapper.convert(id, sourceType, targetType));
    }

    @Test
    void whenConvertingValueThenReturnEntityId() {
        // given
        Long value = 123L;
        TypeDescriptor sourceType = TypeDescriptor.valueOf(Long.class);
        TypeDescriptor targetType = TypeDescriptor.valueOf(TestId.class);

        // when
        Object result = mapper.convert(value, sourceType, targetType);

        // then
        assertNotNull(result);
        assertThat(result).isInstanceOf(TestId.class);
        assertThat(((TestId) result).value()).isEqualTo(value);
    }

    @Test
    void whenConvertingNullValueThenReturnNull() {
        // given
        Long value = null;
        TypeDescriptor sourceType = TypeDescriptor.valueOf(Long.class);
        TypeDescriptor targetType = TypeDescriptor.valueOf(TestId.class);

        // when
        Object result = mapper.convert(value, sourceType, targetType);

        // then
        assertThat(result).isNull();
    }

    @Test
    void whenConversionFailsThenThrowException() {
        // given
        String invalidValue = "invalid";
        TypeDescriptor sourceType = TypeDescriptor.valueOf(String.class);
        TypeDescriptor targetType = TypeDescriptor.valueOf(TestId.class);

        // when / then
        assertThrows(ConversionFailedException.class,
            () -> mapper.convert(invalidValue, sourceType, targetType));
    }

    private static class TestId extends EntityId<Long> {
        private TestId(Long value) {
            super(value);
        }
    }
}