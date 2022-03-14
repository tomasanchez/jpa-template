package com.jpa.app;

import static spark.Spark.port;
import static spark.Spark.staticFileLocation;
import static spark.debug.DebugScreen.enableDebugScreen;
import com.jpa.core.mvc.controller.Controller;
import com.jpa.core.services.ConfigurationLoaderService;
import com.jpa.core.services.ControllerLoaderService;
import spark.TemplateEngine;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class SparkApplication {

    private static final Integer port = 7070;
    private static final TemplateEngine ENGINE = new HandlebarsTemplateEngine();

    public static void main(String[] args) {
        // ! DISABLE DEBUGGER SCREEB WHEN IN PRODUCTION
        new Bootstrap().bootstrapData();
        enableDebugScreen();
        server();
    }

    private static void server() {
        System.out.println("Initializing server...");
        staticFileLocation("/public");
        port(port);
        System.out.println("Listening to port ".concat(port.toString()));
        serveRoutes();
    }

    private static void serveRoutes() {
        // ! INJECT Template Engine into controller class
        Controller.setEngine(ENGINE);

        // ? Call the LoaderServices.
        ConfigurationLoaderService.getService().loadConfigurations("com.jpa");
        ControllerLoaderService.getService().findAll();

    }
}