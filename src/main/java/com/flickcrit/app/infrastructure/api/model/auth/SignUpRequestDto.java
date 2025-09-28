package com.flickcrit.app.infrastructure.api.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flickcrit.app.domain.validation.ValidPassword;
import com.flickcrit.app.infrastructure.api.validation.PasswordsChangeRequest;
import com.flickcrit.app.infrastructure.api.validation.ValidPasswordChangeRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
@ValidPasswordChangeRequest
public record SignUpRequestDto(

    @Email
    @NotBlank(message = "Email is required")
    String email,

    @ValidPassword(message = "Password is not valid")
    String password,

    String passwordRepeat
) implements PasswordsChangeRequest {

    @Override
    @JsonIgnore
    public String getNewPassword() {
        return password;
    }

    @Override
    @JsonIgnore
    public String getNewPasswordRepeat() {
        return passwordRepeat;
    }
}
