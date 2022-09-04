package com.jpa.security.tools.password;


public interface PasswordValidation {

    /**
     * Validates a password criteria.
     * 
     * @param password the entered password to validate
     */
    void validate(String password);
}
