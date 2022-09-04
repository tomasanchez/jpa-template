package com.jpa.exceptions.user;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException(String message) {
        super(String.format("The entered password is invalid: %s", message));
    }

}
