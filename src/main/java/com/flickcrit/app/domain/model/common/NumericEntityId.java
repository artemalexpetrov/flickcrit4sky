package com.flickcrit.app.domain.model.common;

import com.flickcrit.app.utils.ObjectUtils;

/**
 * An abstract base class for numeric entity identifiers. This class is designed
 * to encapsulate a numeric identifier value, ensuring that the value is always
 * valid, i.e., non-null and positive. Primary usage - DB IDs
 */
public abstract class NumericEntityId extends EntityId<Long> {

    protected NumericEntityId(Long value) {
        super(validateValue(value));
    }

    private static Long validateValue(Long value) {
        ObjectUtils.requireNonNull(value, "Numeric ID value must not be null");
        if (value < 1) {
            throw new IllegalArgumentException("Numeric ID value must be positive");
        }

        return value;
    }
}
