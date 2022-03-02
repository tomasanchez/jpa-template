package com.jpa.core.security.auth.exception;

public class AuthorizationException extends RuntimeException {

    /**
     * Constructs an {@code AuthorizationException} with the specified message and no root cause.
     * 
     * @param msg the detail message
     */
    public AuthorizationException(String msg) {
        super(msg);
    }

    /**
     * Constructs an {@code AuthorizationException} with the specified message and root cause.
     * 
     * @param msg the detail message
     * @param cause the root cause
     */
    public AuthorizationException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
