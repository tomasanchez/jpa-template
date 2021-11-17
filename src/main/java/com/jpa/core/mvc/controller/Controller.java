package com.jpa.core.mvc.controller;

import com.jpa.core.mvc.model.Model;
import com.jpa.core.mvc.view.View;
import spark.Request;
import spark.Response;

public abstract class Controller {
    private static Model sharedModel = new Model();
    private View view;

    public Controller() {
        this.view = new View(getName());
        this.onInit();
    }

    public static Model getSharedModel() {
        return sharedModel;
    }

    public static void setSharedModel(Model sharedModel) {
        Controller.sharedModel = sharedModel;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    /**
     * Obtiene el nombre del controlador.
     * 
     * ? Ej: HomeController => home
     * 
     * @return el nombre del controller
     */
    public String getName() {
        return getClass().getSimpleName().toLowerCase().replace("controller", "");
    }

    /**
     * Funcion llamada por única vez, antes de crear el controllador.
     */
    protected abstract void onInit();

    /**
     * Funcion llamada siempre antes de renderizar la Vista.
     * 
     * @param request la HTTP request.
     * @param response la HTTP response.
     */
    protected abstract void onBeforeRendering(Request request, Response response);

    /**
     * Método llamado despues de renderizar la vista.
     * 
     * @param request la spark request
     * @param response la spark response
     */
    protected abstract void onAfterRendering(Request request, Response response);

}
