package com.jpa.core.security.crypto;

public interface PasswordEncoder {

    /**
     * Encodes a raw char sequence.
     * 
     * @param raw the raw password sequence
     * @return an encoding password
     */
    String encode(String raw);

    /**
     * Verifies if a raw char sequence matches a encoded sequence.
     * 
     * @param raw password
     * @param password encoded sequence
     * @return true if the raw passowrd, after encoding, matches the encoded password
     */
    boolean matches(String raw, String password);

}
