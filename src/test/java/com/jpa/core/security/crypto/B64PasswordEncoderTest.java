package com.jpa.core.security.crypto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class B64PasswordEncoderTest {

    private B64PasswordEncoder encoder;
    private String password;

    @BeforeEach
    void setup() {
        encoder = new B64PasswordEncoder();
        password = "A-g3n3r1c-password-#4-Te$71NG!";
    }

    @Test
    void whenAPasswordIsEconded_then_resultsInAEncodedString() {
        Assertions.assertNotNull(encoder.encode(password));
    }

    @Test
    void whenANullPassowrdIsEncoded_then_NullPtrExceptionIsTrhown() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            encoder.encode(null);
        });
    }

    @Test
    void whenAPwIsEnconded_then_matchesItSelfDecoded() {
        Assertions.assertTrue(encoder.matches(password, encoder.encode(password)),
                "Password does not match");
    }
}
