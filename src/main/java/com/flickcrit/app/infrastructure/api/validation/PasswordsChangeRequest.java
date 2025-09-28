package com.flickcrit.app.infrastructure.api.validation;

/**
 * An interface representing a request to change passwords.
 * This interface defines methods to retrieve the new password
 * and its repeated confirmation for validation purposes.
 */
public interface PasswordsChangeRequest {

    String getNewPassword();

    String getNewPasswordRepeat();
}
