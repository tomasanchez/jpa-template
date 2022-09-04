package com.jpa.core.services;

import com.jpa.controller.BaseController;
import com.jpa.core.mvc.controller.Controller;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class ControllerLoaderService {

    private static ControllerLoaderService instance;
    private Map<String, Controller> controllers = new HashMap<>();

    /* =========================================================== */
    /* Getters & Setter ------------------------------------------ */
    /* =========================================================== */

    /**
     * Gets the Controller Loader Service instance.
     *
     * @return the singleton instance
     */
    public static ControllerLoaderService getService() {

        if (instance == null) {
            instance = new ControllerLoaderService();
            BaseController.getEngine();
        }

        return instance;
    }

    /* =========================================================== */
    /* Service Interface ----------------------------------------- */
    /* =========================================================== */

    /**
     * Locates a specific controller.
     *
     * @param name of the controller to be found
     * @return a Controller instance or null if not found
     */
    public Controller find(String name) {
        return fetchWhenEmpty().get(name);
    }

    /**
     * Retrieves all controllers available.
     */
    public void findAll() {
        fetchWhenEmpty();
    }

    /* =========================================================== */
    /* Internal Methods ------------------------------------------ */
    /* =========================================================== */

    /**
     * Verifies if the controller map is empty, if it is triggers the fetch event.
     *
     * @return a map of controllers
     */
    private Map<String, Controller> fetchWhenEmpty() {
        return controllers.isEmpty() ? fetchControllers() : this.controllers;
    }

    /**
     * Uses reflections to get all controllers available on a controller package.
     *
     * @return a map of controllers.
     */
    private Map<String, Controller> fetchControllers() {
        Arrays.stream(Package.getPackages()).map(Package::getName)
                .filter(p -> p.contains("controller") && !p.contains("core.mvc.controller"))
                .findFirst().ifPresent(controllerPackage -> {

                    System.out.println("\033[0;36mUsing controllers of package "
                            .concat(controllerPackage).concat("\033[0m"));

                    Reflections reflections = new Reflections(controllerPackage);
                    Set<Class<? extends Controller>> classes =
                            reflections.getSubTypesOf(Controller.class);

                    // Moves the not found controller to the end of the list of controllers.
                    // ! This is needed as its endpoint must be the last to be initialized.
                    classes.stream().filter(c -> c.getSimpleName().equals("NotFoundController"))
                            .findFirst().ifPresent((c) -> {
                                classes.remove(c);
                                classes.add(c);
                            });

                    this.controllers = classes.stream()
                            .filter(c -> !Modifier.isAbstract(c.getModifiers())).map(c -> {
                                Controller controller;
                                try {
                                    controller = c.getDeclaredConstructor().newInstance();
                                    System.out.println("Controller ".concat(controller.getName())
                                            .concat(" initializes!"));
                                } catch (Exception e) {
                                    controller = null;
                                    e.printStackTrace();
                                }
                                return controller;
                            }).filter(controller -> !Objects.isNull(controller))
                            .collect(Collectors.toMap(Controller::getShortName, c -> c));
                });


        if (this.controllers.isEmpty()) {
            System.out.println("\033[0;31mNo controllers were found\033[0m");
        }

        return this.controllers;
    }

}
