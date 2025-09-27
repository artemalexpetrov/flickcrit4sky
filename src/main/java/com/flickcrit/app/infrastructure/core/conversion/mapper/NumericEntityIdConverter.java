package com.flickcrit.app.infrastructure.core.conversion.mapper;

import com.flickcrit.app.domain.model.common.NumericEntityId;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class NumericEntityIdConverter implements GenericConverter {

    private final EntityIdMapper mapper;

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Set.of(
            new ConvertiblePair(String.class, NumericEntityId.class),
            new ConvertiblePair(Long.class, NumericEntityId.class)
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

        try {
            Long numericValue = getNumericValue(source);
            TypeDescriptor numericDescriptor = TypeDescriptor.forObject(numericValue);
            return mapper.convert(numericValue, numericDescriptor, targetType);
        } catch (NumberFormatException e) {
            throw new ConversionFailedException(sourceType, targetType, source, e);
        }
    }

    private Long getNumericValue(Object source) {
        if (source instanceof Long longValue) {
            return longValue;
        }

        return Long.valueOf(source.toString());
    }
}
