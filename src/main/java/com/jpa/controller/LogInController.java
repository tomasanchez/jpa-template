package com.jpa.controller;

import java.util.Objects;
import com.jpa.core.mvc.controller.routing.GetMapping;
import com.jpa.core.mvc.controller.routing.PostMapping;
import com.jpa.security.services.JwtSessionAuthenticationService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class LogInController extends BaseController {

    private JwtSessionAuthenticationService authService =
            new JwtSessionAuthenticationService(getSecurityContext().getAuthenticationManager());

    /* =========================================================== */
    /* Lifecycle methods ----------------------------------------- */
    /* =========================================================== */

    @Override
    protected void onInit() {
        // Sets not LoggedIn in the View Model.
        modelAuthenticate(null);
    }

    @Override
    protected void onBeforeRendering(Request request, Response response) {
        if (isLogged(request)) {
            navTo(response, "home");
        }
    }

    @Override
    protected void onAfterRendering(Request request, Response response) {}

    /* =========================================================== */
    /* Request Handling ------------------------------------------ */
    /* =========================================================== */

    @GetMapping
    private ModelAndView loginPage(Request request, Response response) {
        return getModelAndView();
    }

    @PostMapping
    protected ModelAndView onPost(Request request, Response response) {

        if (Objects.isNull(request.queryParams("uid"))) {

            try {
                onLogIn(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            onLogOut(request, response);
        }

        return getModelAndView();
    }

    /* =========================================================== */
    /* Event Handlers -------------------------------------------- */
    /* =========================================================== */

    /**
     * Log In Event Handler, triggered when an user request POST on the login form.
     * 
     * @param request the spark HTTP request object
     * @param response the spark HTTP response object
     */
    private void onLogIn(Request request, Response response) throws Exception {

        authService.attemptAuthentication(request, response, this::onSuccessfulAuthentication,
                this::onUnsuccessfulAuthenticaiton);

    }

    /**
     * Log Out event handler, triggered when an user request POST on a log out form.
     * 
     * @param request the spark HTTP request object
     * @param response the spark HTTP response object
     */
    private void onLogOut(Request request, Response response) {

        try {
            authenticationMap.remove((String) retrieveAuthentication(request).get().getPrincipal());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            modelAuthenticate(null);
            request.session().invalidate();
            navTo(response, "home");
        }
    }


    private void onSuccessfulAuthentication(Request request, Response response) {

        try {
            authenticationMap.put((String) retrieveAuthentication(request).get().getPrincipal(),
                    onRetrieveUser(request).orElse(null));
        } catch (Exception e) {
            e.printStackTrace();
        }

        navTo(response, "home");
    }

    private void onUnsuccessfulAuthenticaiton(Request request, Response response) {
        modelAuthenticate(null);
        String username = request.queryParams("user");
        getView().getModel().set("isValid", "is-invalid").set("username", username);
    }

}
