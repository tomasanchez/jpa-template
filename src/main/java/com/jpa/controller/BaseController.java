package com.jpa.controller;

import com.jpa.core.mvc.controller.Controller;
import spark.Request;
import spark.Response;

public abstract class BaseController extends Controller {

    /* =========================================================== */
    /* HTTP Request Handling ------------------------------------- */
    /* =========================================================== */

    @Override
    protected Object getResponse(Request request, Response response) {
        return null;
    }

}
