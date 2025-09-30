package com.flickcrit.app.domain.validation;


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
class PasswordConstraintValidatorTest {

    private static final String VALID_PASSWORD = "ValidPass123!";
    private static final String INVALID_PASSWORD = "weak";

    @Mock(strictness = Mock.Strictness.LENIENT)
    private ConstraintValidatorContext context;

    @Mock(strictness = Mock.Strictness.LENIENT)
    private ConstraintViolationBuilder builder;

    private PasswordConstraintValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PasswordConstraintValidator();
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
    }

    @Test
    void givenNullValueWhenIsValidExpectFalse() {
        assertThat(validator.isValid(null, context)).isFalse();
        verifyNoInteractions(context);
    }

    @Test
    void givenValidPasswordWhenIsValidExpectTrue() {
        assertThat(validator.isValid(VALID_PASSWORD, context)).isTrue();
        verifyNoInteractions(context);
    }

    @Test
    void givenInvalidPasswordWhenIsValidExpectFalse() {
        when(builder.addConstraintViolation()).thenReturn(context);

        assertThat(validator.isValid(INVALID_PASSWORD, context)).isFalse();

        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate(anyString());
        verify(builder).addConstraintViolation();
    }
}