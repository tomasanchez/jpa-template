package com.jpa.core.security.crypto;

import java.util.regex.Pattern;

public class BCryptPasswordEncoder implements PasswordEncoder {

    private Pattern BCRYPT_PATTERN =
            Pattern.compile("\\A\\$2(a|y|b)?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}");

    @Override
    public String encode(String raw) {

        checkNullRaw(raw);

        String salt = BCrypt.gensalt();

        return BCrypt.hashpw(raw, salt);
    }


    @Override
    public boolean matches(String raw, String password) {

        checkNullRaw(raw);

        if (password == null || password.isEmpty()) {
            return false;
        }

        if (!this.BCRYPT_PATTERN.matcher(password).matches()) {
            return false;
        }

        return BCrypt.checkpw(raw, password);
    }


    private void checkNullRaw(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("The raw string cannot be null");
        }
    }

}
