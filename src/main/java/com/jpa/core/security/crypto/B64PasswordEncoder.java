package com.jpa.core.security.crypto;

import java.util.Base64;
import java.util.Objects;

/**
 * A generic encoder.
 * 
 * @author Tomás Sánchez
 */
public class B64PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(String raw) {

        return Base64.getEncoder().encodeToString(raw.getBytes());
    }

    @Override
    public boolean matches(String raw, String password) {

        if (Objects.isNull(raw) || Objects.isNull(password)) {
            return false;
        }

        return new String(Base64.getDecoder().decode(password)).equals(raw);
    }

}
