package com.flickcrit.app.domain.model.user;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "test@example.com",
        "user.name@domain.com",
        "firstname.lastname@domain.co.uk"
    })
    void shouldCreateEmailFromValidAddress(String address) {
        var email = Email.of(address);
        assertThat(email.value()).isEqualTo(address);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "invalid",
        "@domain.com",
        "user@",
        "user@.",
        ".user@domain.com",
        "user@domain",
        "user name@domain.com"
    })
    void shouldThrowExceptionForInvalidAddress(String address) {
        assertThatThrownBy(() -> Email.of(address))
            .isInstanceOf(IllegalArgumentException.class);
    }
}