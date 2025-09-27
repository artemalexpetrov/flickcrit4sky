package com.flickcrit.app.infrastructure.core.conversion.mapper;

import com.flickcrit.app.domain.model.common.NumericEntityId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NumericEntityIdConverterTest {

    @Mock
    private EntityIdMapper entityIdMapper;

    @InjectMocks
    private NumericEntityIdConverter converter;

    @Test
    void givenStringValueWhenConvertThenReturnsNumericEntityId() {
        // given
        String source = "123";
        TypeDescriptor sourceType = TypeDescriptor.valueOf(String.class);
        TypeDescriptor targetType = TypeDescriptor.valueOf(NumericEntityId.class);
        NumericEntityId expected = mock(NumericEntityId.class);

        when(entityIdMapper
            .convert(123L, TypeDescriptor.valueOf(Long.class), targetType))
            .thenReturn(expected);

        // when
        Object result = converter.convert(source, sourceType, targetType);

        // then
        assertNotNull(result);
        assertSame(expected, result);
        verify(entityIdMapper).convert(123L, TypeDescriptor.valueOf(Long.class), targetType);
    }

    @Test
    void givenLongValueWhenConvertThenReturnsNumericEntityId() {
        // given
        Long source = 456L;
        TypeDescriptor sourceType = TypeDescriptor.valueOf(Long.class);
        TypeDescriptor targetType = TypeDescriptor.valueOf(NumericEntityId.class);
        NumericEntityId expected = mock(NumericEntityId.class);

        when(entityIdMapper.convert(456L, TypeDescriptor.valueOf(Long.class), targetType)).thenReturn(expected);

        // when
        Object result = converter.convert(source, sourceType, targetType);

        // then
        assertNotNull(result);
        assertSame(expected, result);
        verify(entityIdMapper).convert(456L, TypeDescriptor.valueOf(Long.class), targetType);
    }

    @Test
    void givenNullValueWhenConvertThenReturnsNull() {
        // given
        Object source = null;
        TypeDescriptor sourceType = TypeDescriptor.valueOf(String.class);
        TypeDescriptor targetType = TypeDescriptor.valueOf(NumericEntityId.class);

        // when
        Object result = converter.convert(source, sourceType, targetType);

        // then
        assertNull(result);
        verifyNoInteractions(entityIdMapper);
    }

    @Test
    void givenInvalidStringValueWhenConvertThenThrowsConversionFailedException() {
        // given
        String source = "invalid";
        TypeDescriptor sourceType = TypeDescriptor.valueOf(String.class);
        TypeDescriptor targetType = TypeDescriptor.valueOf(NumericEntityId.class);

        // when - then
        assertThrows(ConversionFailedException.class, () -> converter.convert(source, sourceType, targetType));
        verifyNoInteractions(entityIdMapper);
    }

    @Test
    void givenInvalidTypeWhenConvertThenThrowsConversionFailedException() {
        // given
        Object source = new Object();
        TypeDescriptor sourceType = TypeDescriptor.valueOf(Object.class);
        TypeDescriptor targetType = TypeDescriptor.valueOf(NumericEntityId.class);

        // when - then
        assertThrows(ConversionFailedException.class, () -> converter.convert(source, sourceType, targetType));
        verifyNoInteractions(entityIdMapper);
    }
}