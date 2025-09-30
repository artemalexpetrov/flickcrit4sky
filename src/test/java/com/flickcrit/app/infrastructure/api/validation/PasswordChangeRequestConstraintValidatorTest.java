package com.flickcrit.app.infrastructure.api.validation;


import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordChangeRequestConstraintValidatorTest {

    @Mock(strictness = Mock.Strictness.LENIENT)
    private ConstraintValidatorContext context;

    @Mock(strictness = Mock.Strictness.LENIENT)
    private ConstraintViolationBuilder builder;

    private PasswordChangeRequestConstraintValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PasswordChangeRequestConstraintValidator();
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
    }

    @Test
    void shouldReturnTrueWhenValueIsNull() {
        boolean result = validator.isValid(null, context);

        assertThat(result).isTrue();
        verifyNoInteractions(context);
    }

    @Test
    void shouldReturnTrueWhenPasswordsMatch() {
        PasswordsChangeRequest request = new DummyPasswordsChangeRequest("password123", "password123");
        assertThat(validator.isValid(request, context)).isTrue();
        verifyNoInteractions(context);
    }

    @Test
    void shouldReturnFalseWhenPasswordsDoNotMatch() {
        PasswordsChangeRequest request = new DummyPasswordsChangeRequest("password123", "differentPassword");

        boolean result = validator.isValid(request, context);

        assertThat(result).isFalse();
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate(anyString());
        verify(builder).addConstraintViolation();
    }

    @Getter
    @RequiredArgsConstructor
    private static class DummyPasswordsChangeRequest implements PasswordsChangeRequest{
        private final String newPassword;
        private final String newPasswordRepeat;
    }
}