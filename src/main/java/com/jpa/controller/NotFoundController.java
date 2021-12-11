package com.jpa.controller;

import com.jpa.core.mvc.controller.Controller;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class NotFoundController extends Controller {

    /* =========================================================== */
    /* Overridables ---------------------------------------------- */
    /* =========================================================== */

    @Override
    public String getEndPoint(Boolean useId) {
        return "*";
    }

    @Override
    protected ModelAndView onGet(Request request, Response response) {
        super.onBeforeBeforeRendering(request, response);
        if (!request.pathInfo().startsWith("/static")) {
            response.status(404);
            return super.onGet(request, response);
        } else {
            return null;
        }
    }

    @Override
    protected Object onGetResponse(Request request, Response response) {
        response.status(404);
        return null;
    }

    /* =========================================================== */
    /* Lifecycle methods ----------------------------------------- */
    /* =========================================================== */

    @Override
    protected void onInit() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onBeforeBeforeRendering(Request request, Response response) {
        // Avoids using the same call for all controllers.
        return;
    }

    @Override
    protected void onBeforeRendering(Request request, Response response) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onAfterRendering(Request request, Response response) {
        // TODO Auto-generated method stub

    }

}
