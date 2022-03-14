package com.jpa.exceptions.user;

public class UserAlreadyExistsException extends RuntimeException {


    /**
     * Constructs an {@code UserAlreadyExistsException} with the specified message and no root
     * cause.
     * 
     * @param msg the detail message
     */
    public UserAlreadyExistsException(String msg) {
        super(msg);
    }

    /**
     * Constructs an {@code UserAlreadyExistsException} with the specified message and root cause.
     * 
     * @param msg the detail message
     * @param cause the root cause
     */
    public UserAlreadyExistsException(String msg, Throwable cause) {
        super(msg, cause);
    }


}
