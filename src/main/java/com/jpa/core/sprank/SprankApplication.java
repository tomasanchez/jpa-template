package com.jpa.core.sprank;

import static spark.Spark.port;
import static spark.Spark.staticFileLocation;
import static spark.debug.DebugScreen.enableDebugScreen;
import com.jpa.core.mvc.controller.Controller;
import com.jpa.core.services.ConfigurationLoaderService;
import com.jpa.core.services.ControllerLoaderService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spark.TemplateEngine;
import spark.template.handlebars.HandlebarsTemplateEngine;


/**
 * Application made with Spark microframework using some Springboot framework ideas.
 * 
 * By defaults starts with port 8080, enabled debug screen and HandlebarsTemplateEngine.
 * 
 * @author Tomas Sanchez <tosanchez@frba.utn.edu.ar>
 * @version 2.x
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class SprankApplication {

    private int port = 8080;

    private boolean debug = true;

    private TemplateEngine engine = new HandlebarsTemplateEngine();


    /**
     * Creates a new pre-built "Sprank" application server for server-side rendering.
     * 
     * @param port the port to listen
     * @param debugMode if debug screen should be enabled
     */
    public SprankApplication(int port, boolean debugMode) {
        this.port = port;
        this.debug = debugMode;
    }

    /**
     * Runs the Spangk application.
     */
    public void run() {
        onInitSpark();
        onInitSpangk();
    }


    /**
     * Initializes Spark variables.
     */
    private void onInitSpark() {

        if (debug) {
            System.out.println("[DEBUG MODE] - Enabled debug screen");
            enableDebugScreen();
        }

        System.out.println("Initializing server...");
        staticFileLocation("/public");
        port(port);
        System.out.println(String.format("Listening to port %d.", port));
    }

    /**
     * Initializes custom micro-framework.
     */
    private void onInitSpangk() {
        Controller.setEngine(engine);
        this.getClass().getCanonicalName();
        ConfigurationLoaderService.getService().loadConfigurations(getClassPath());
        ControllerLoaderService.getService().findAll();
    }


    /**
     * Obtains a Maven Project classpath for Configuration files.
     * 
     * @return the classpath
     */
    private String getClassPath() {
        String canonicalName = this.getClass().getCanonicalName();

        String[] packages = canonicalName.split("\\.");


        if (packages.length > 2) {
            return String.format("%s.%s", packages[0], packages[1]);
        }

        return String.format("%s", packages[0]);
    }
}
