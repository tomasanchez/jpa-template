package com.jpa.core.security.auth.exception;

/**
 * A superclass for all exceptions related to authentication.
 */
public abstract class AuthenticationException extends RuntimeException {


    /**
     * Constructs an {@code AuthenticationException} with the specified message and no root cause.
     * 
     * @param msg the detail message
     */
    public AuthenticationException(String msg) {
        super(msg);
    }

    /**
     * Constructs an {@code AuthenticationException} with the specified message and root cause.
     * 
     * @param msg the detail message
     * @param cause the root cause
     */
    public AuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
