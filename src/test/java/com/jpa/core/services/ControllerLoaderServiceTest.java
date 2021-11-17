package com.jpa.core.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class ControllerLoaderServiceTest {


    @Test
    void testGetControllers() {
        ControllerLoaderService service = new ControllerLoaderService();
        String controllerName = "home";
        assertEquals(controllerName, service.find(controllerName).getShortName());
    }
}
