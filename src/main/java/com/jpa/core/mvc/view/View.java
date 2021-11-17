package com.jpa.core.mvc.view;

import com.jpa.core.mvc.model.Model;

public class View {

    private String viewName;
    private String path;
    private Model model = new Model();

    private static final String FILE_EXTENSION = ".html.hbs";

    public View(String name) {
        this.viewName = name;
        this.path = name.concat(FILE_EXTENSION);
    }

    /* =========================================================== */
    /* Getters & Setter ------------------------------------------ */
    /* =========================================================== */

    /**
     * Obtains the view default file extension.
     * 
     * @return the file extension
     */
    public static String getFileExtension() {
        return FILE_EXTENSION;
    }

    /**
     * Obtains the current view name.
     * 
     * @return the view name
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * Sets the view name.
     * 
     * @param name a name to be set
     * @return the renamed view
     */
    public View setViewName(String name) {
        this.viewName = name;
        return this;
    }

    /**
     * Obtains the endpoint.
     * 
     * @return the view uri
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the view's endpoint.
     * 
     * @param path the new endpoint
     * @return the view
     */
    public View setPath(String path) {
        this.path = path;
        return this;
    }

    /**
     * Obtains the view model.
     * 
     * @return the viewModel.
     */
    public Model getModel() {
        return model;
    }

    /**
     * Sets the view model.
     * 
     * @param model the model to be set
     * @return the view
     */
    public View setModel(Model model) {
        this.model = model;
        return this;
    }

    /**
     * Sets a new model with a specified name.
     * 
     * @param modelName
     * @param model
     * @return
     */
    public View setModel(String modelName, Model model) {
        this.getModel().set(modelName, model);
        return this;
    }

}
