package com.jpa.core.mvc.controller;

import com.jpa.core.mvc.model.Model;
import com.jpa.core.mvc.view.View;
import spark.Request;
import spark.Response;

/**
 * A generic controller implementation for the TS-JPA Model-View-Controller concept.
 * 
 * @author Tomás Sánchez
 * @since 1.0
 */
public abstract class Controller {

    private static Model sharedModel = new Model();
    private View view;

    public Controller() {
        this.view = new View(getName());
        this.onInit();
    }

    /* =========================================================== */
    /* Getters & Setter ------------------------------------------ */
    /* =========================================================== */

    /**
     * Gets the shared model among controllers.
     * 
     * @return A shared model
     */
    public static Model getSharedModel() {
        return sharedModel;
    }

    /**
     * Sets a shared model among all controllers.
     * 
     * @param sharedModel a model to be shared between controllers
     * @return the shared model
     */
    public static Model setSharedModel(Model sharedModel) {
        Controller.sharedModel = sharedModel;
        return getSharedModel();
    }

    /**
     * Retrieves the current controller view.
     * 
     * @return the view of the current controller
     */
    public View getView() {
        return view;
    }

    /**
     * Sets a controller view.
     * 
     * @param view
     * @return
     */
    public Controller setView(View view) {
        this.view = view;
        return this;
    }

    /**
     * Obtains the controller name.
     * 
     * ? Example: HomeController => home
     * 
     * @return the controller name
     */
    public String getName() {
        return getClass().getSimpleName().toLowerCase().replace("controller", "");
    }

    /* =========================================================== */
    /* Lifecycle methods ----------------------------------------- */
    /* =========================================================== */

    /**
     * This method is called upon initialization of the View. The controller can perform its
     * internal setup in this hook. It is only called once per View instance, unlike the
     * onBeforeRendering and onAfterRendering hooks.
     */
    protected abstract void onInit();

    /**
     * 
     * This method is called every time the View is rendered, before the Renderer is called and the
     * HTML is placed in the DOM-Tree. It can be used to perform clean-up-tasks before re-rendering.
     * 
     * @param request the Spark HTTP request object
     * @param response the Spark HTTP response object
     */
    protected abstract void onBeforeRendering(Request request, Response response);

    /**
     * 
     * This method is called every time the View is rendered, after the HTML is placed in the
     * DOM-Tree. It can be used to apply additional changes to the Model after the Renderer has
     * finished.
     * 
     * @param request the spark HTTP request object
     * @param response the spark HTTP response object
     */
    protected abstract void onAfterRendering(Request request, Response response);

}
