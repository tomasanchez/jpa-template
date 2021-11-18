package com.jpa.core.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import com.jpa.core.mvc.controller.Controller;
import org.reflections.Reflections;

public class ControllerLoaderService {


    private static ControllerLoaderService instance;

    private Map<String, Controller> controllers = new HashMap<String, Controller>();

    /* =========================================================== */
    /* Getters & Setter ------------------------------------------ */
    /* =========================================================== */

    /**
     * Gets the
     * 
     * @return
     */
    public static ControllerLoaderService getService() {

        if (instance == null) {
            instance = new ControllerLoaderService();
        }

        return instance;
    }

    /* =========================================================== */
    /* Service Interface ----------------------------------------- */
    /* =========================================================== */

    public Controller find(String name) {
        return fetchWhenEmpty().get(name);
    }

    public List<Controller> findAll() {
        return fetchWhenEmpty().values().stream().collect(Collectors.toList());
    }

    /* =========================================================== */
    /* Internal Methods ------------------------------------------ */
    /* =========================================================== */

    private Map<String, Controller> fetchWhenEmpty() {
        return controllers.isEmpty() ? fetchControllers() : this.controllers;
    }

    private Map<String, Controller> fetchControllers() {
        Reflections reflections = new Reflections("com.jpa.controller");
        Set<Class<? extends Controller>> classes = reflections.getSubTypesOf(Controller.class);
        this.controllers =
                classes.stream().filter(c -> !Modifier.isAbstract(c.getModifiers())).map(c -> {
                    Controller controller = null;
                    try {
                        controller = c.getDeclaredConstructor().newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                    return controller;
                }).filter(controller -> !Objects.isNull(controller))
                        .collect(Collectors.toMap(c -> c.getShortName(), c -> c));

        return this.controllers;
    }

}
