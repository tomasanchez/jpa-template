package com.jpa.controller;

import com.jpa.core.mvc.controller.routing.GetMapping;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class HomeController extends BaseController {

    /* =========================================================== */
    /* Overridables ---------------------------------------------- */
    /* =========================================================== */

    @Override
    public String getEndPoint(Boolean useId) {
        return "/";
    }

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
    /* HTTP Request Handling ------------------------------------- */
    /* =========================================================== */

    /* ===================== */
    /* GET REQUEST --------- */
    /* ===================== */


    @GetMapping(path = "/")
    protected ModelAndView getHome(Request request, Response response) {
        return getModelAndView();
    }


    /* =========================================================== */
    /* Internal Methods ------------------------------------------ */
    /* =========================================================== */

}
