package com.jpa.core.security.auth.exception;

/**
 * Corresponds to HTTP - 403 error.
 */
public class ForbiddenException extends AuthenticationException {

    /**
     * Constructs an {@code ForbiddenException} with the specified message and no root cause.
     * 
     * @param msg the detail message
     */
    public ForbiddenException(String msg) {
        super(msg);
    }

    /**
     * Constructs an {@code ForbiddenException} with the specified message and root cause.
     * 
     * @param msg the detail message
     * @param cause the root cause
     */
    public ForbiddenException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
