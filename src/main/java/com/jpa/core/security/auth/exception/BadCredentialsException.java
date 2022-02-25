package com.jpa.core.security.auth.exception;

/**
 * Thrown if an authentication request is rejected due to invalid credentials.
 */
public class BadCredentialsException extends AuthenticationException {

    /**
     * Constructs a <code>BadCredentialsException</code> with the specified message.
     * 
     * @param msg the detail message
     */
    public BadCredentialsException(String msg) {
        super(msg);
    }

    /**
     * Constructs a <code>BadCredentialsException</code> with the specified message and root cause.
     * 
     * @param msg the detail message
     * @param cause root cause
     */
    public BadCredentialsException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
