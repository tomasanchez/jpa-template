package com.jpa.security.tools.password;

import com.jpa.exceptions.user.InvalidPasswordException;

public class PasswordLengthValidation implements PasswordValidation {

    public static final int MIN_PASSWORD_LENGTH = 8;

    @Override
    public void validate(String password) {

        if (password == null) {
            throw new InvalidPasswordException("Password must not be null");
        }

        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new InvalidPasswordException(String.format(
                    "Password requieres atleast %d characters, entered password has %d.",
                    MIN_PASSWORD_LENGTH, password.length()));
        }

    }

}
