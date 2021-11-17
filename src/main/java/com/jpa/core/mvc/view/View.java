package com.jpa.core.mvc.view;

import com.jpa.core.mvc.model.Model;

public class View {

    private String name;
    private String path;
    private Model model = new Model();

    private static final String FILE_EXTENSION = ".html.hbs";

    public View(String name) {
        this.name = name;
        this.path = name.concat(FILE_EXTENSION);
    }

    public static String getFileExtension() {
        return FILE_EXTENSION;
    }

    public String getName() {
        return name;
    }

    public View setName(String name) {
        this.name = name;
        return this;
    }

    public String getPath() {
        return path;
    }

    public View setPath(String path) {
        this.path = path;
        return this;
    }

    public Model getModel() {
        return model;
    }

    public View setModel(Model model) {
        this.model = model;
        return this;
    }

}
