package com.jpa.security.tools.password;

import com.jpa.exceptions.user.InvalidPasswordException;

import java.util.Collections;
import java.util.List;

public class PasswordValidator {

    List<PasswordValidation> validations = Collections.singletonList(new PasswordLengthValidation());

    /**
     * Verifies if a password is valid.
     * 
     * @param password to be validated
     * @throws InvalidPasswordException when the password does not pass all the validations
     */
    public void validatePassword(String password) {

        validations.forEach(v -> v.validate(password));
    }

}
