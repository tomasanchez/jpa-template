package com.jpa.i18n;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class ResourceBundleTest {

    @Test
    void i18nUp() {
        assertNotNull(new ResourceBundle().getText("test"));
    }

    @Test
    void i18nMultipleLanguages() {
        ResourceBundle bundleEN = new ResourceBundle();
        ResourceBundle bundleFR = new ResourceBundle("fr");
        assertNotEquals(bundleEN.getText("test"), bundleFR.getText("test"));
    }
}
