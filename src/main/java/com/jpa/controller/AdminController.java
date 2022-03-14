package com.jpa.controller;

import com.jpa.core.mvc.controller.routing.GetMapping;
import com.jpa.core.security.auth.Secured;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class AdminController extends BaseController {

    /* =========================================================== */
    /* Lifecycle methods ----------------------------------------- */
    /* =========================================================== */

    @Override
    protected void onInit() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onBeforeRendering(Request request, Response response) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onAfterRendering(Request request, Response response) {
        // TODO Auto-generated method stub

    }

    /* =========================================================== */
    /* Request Handling ------------------------------------------ */
    /* =========================================================== */

    @GetMapping
    @Secured(roles = "ROLE_STAFF")
    public ModelAndView getPage(Request request, Response response) {
        return getModelAndView();
    }

}
