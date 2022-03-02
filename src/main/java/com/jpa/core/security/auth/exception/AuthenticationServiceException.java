package com.jpa.core.security.auth.exception;

public class AuthenticationServiceException extends AuthenticationException {

    /**
     * Constructs an <code>AuthenticationServiceException</code> with the specified message.
     * 
     * @param msg the detail message
     */
    public AuthenticationServiceException(String msg) {
        super(msg);
    }

    /**
     * Constructs an <code>AuthenticationServiceException</code> with the specified message and root
     * cause.
     * 
     * @param msg the detail message
     * @param cause root cause
     */
    public AuthenticationServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
