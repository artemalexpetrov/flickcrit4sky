package com.flickcrit.app.domain.validation;

import com.flickcrit.app.domain.model.rating.RatingValue;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingConstraintValidatorTest {

    @Mock(strictness = Mock.Strictness.LENIENT)
    private ConstraintValidatorContext context;

    @Mock(strictness = Mock.Strictness.LENIENT)
    private ConstraintViolationBuilder builder;

    private RatingConstraintValidator validator;

    @BeforeEach
    void setUp() {
        validator = new RatingConstraintValidator();
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
    }

    @Test
    void givenNullValueWhenIsValidExpectTrue() {
        assertThat(validator.isValid(null, context)).isTrue();
        verifyNoInteractions(context);
    }

    @Test
    void givenValidValueWhenIsValidExpectTrue() {
        assertThat(validator.isValid(RatingValue.MIN_RATING, context)).isTrue();
        assertThat(validator.isValid(RatingValue.MAX_RATING, context)).isTrue();
        assertThat(validator.isValid((RatingValue.MAX_RATING + RatingValue.MIN_RATING) / 2, context)).isTrue();
        verifyNoInteractions(context);
    }

    @Test
    void givenInvalidValueWhenIsValidExpectFalse() {
        when(builder.addConstraintViolation()).thenReturn(context);

        assertThat(validator.isValid(RatingValue.MIN_RATING - 1, context)).isFalse();
        assertThat(validator.isValid(RatingValue.MAX_RATING + 1, context)).isFalse();

        verify(context, times(2)).disableDefaultConstraintViolation();
        verify(context, times(2)).buildConstraintViolationWithTemplate(anyString());
        verify(builder, times(2)).addConstraintViolation();
    }
}