package com.jpa.core.services;

import java.util.Set;
import com.jpa.core.config.Configuration;
import com.jpa.core.config.SimpleConfiguration;
import org.reflections.Reflections;

/**
 * @author Tomás Sánchez
 */
public class ConfigurationLoaderService {

    private static ConfigurationLoaderService instance;

    /* =========================================================== */
    /* Getters & Setter ------------------------------------------ */
    /* =========================================================== */

    /**
     * Gets the ConfigurationLoaderService instance.
     * 
     * @return the singleton instance
     */
    public static ConfigurationLoaderService getService() {

        if (instance == null) {
            instance = new ConfigurationLoaderService();
        }

        return instance;
    }

    /* =========================================================== */
    /* Service Interface ----------------------------------------- */
    /* =========================================================== */

    /**
     * Loads and sets all configurations.
     * 
     * @param classPath the package where to find configurations.
     */
    public void loadConfigurations(String classPath) {

        Reflections reflections = new Reflections(classPath);
        Set<Class<? extends SimpleConfiguration>> classes =
                reflections.getSubTypesOf(SimpleConfiguration.class);

        classes.stream().filter(c -> c.isAnnotationPresent(Configuration.class)).forEach(c -> {
            try {
                SimpleConfiguration config = c.getDeclaredConstructor().newInstance();
                config.configure();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

}
