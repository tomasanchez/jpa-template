package com.jpa.app;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.staticFileLocation;
import static spark.debug.DebugScreen.enableDebugScreen;

public class Router {

    private static final Integer port = 7070;

    public static void main(String[] args) {
        // ! DISABLE DEBUGGER SCREEB WHEN IN PRODUCTION
        enableDebugScreen();
        server();
    }

    private static void server() {
        System.out.println("Initializing server...");
        port(port);
        System.out.println("Listening to port ".concat(port.toString()));
        staticFileLocation("/public");
        serveRoutes();
    }

    private static void serveRoutes() {
        get("/", (req, res) -> "Hello world!");
    }
}
