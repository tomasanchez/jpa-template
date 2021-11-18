package com.jpa.controller;

import java.util.Map;
import com.jpa.core.mvc.controller.Controller;


public abstract class BaseController extends Controller {


    /* =========================================================== */
    /* Convenience Methods --------------------------------------- */
    /* =========================================================== */

    /**
     * Convenience method for getting the view model map.
     * 
     * @return the View Model
     */
    public Map<String, Object> getModel() {
        return this.getView().getModel().getData();
    }

    /**
     * Convenience method for getting a value from i18n.
     * 
     * @param text the key value
     * @return the text value of the key
     */
    public String getText(String text) {
        return (String) Controller.getI18n().getText(text);
    }
}
