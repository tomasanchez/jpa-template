package com.jpa.core.mvc.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ModelTest {

    private Model model;

    @BeforeEach
    void setUp() {
        model = new Model();
    }

    @Test
    void canSetAndGet() {
        Object object = new Object();
        model.set("test", object);
        assertEquals(object, model.get("test"));
    }

}
