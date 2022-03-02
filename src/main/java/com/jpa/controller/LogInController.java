package com.jpa.controller;

import java.util.Objects;
import com.jpa.config.SecurityConfig;
import com.jpa.core.config.WebSecurityConfig;
import com.jpa.core.mvc.controller.routing.GetMapping;
import com.jpa.core.mvc.controller.routing.PostMapping;
import com.jpa.security.services.JwtSessionAuthenticationService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class LogInController extends BaseController {

    private WebSecurityConfig wsc = SecurityConfig.getInstance();

    private JwtSessionAuthenticationService authService =
            new JwtSessionAuthenticationService(wsc.getAuthenticationManager());

    /* =========================================================== */
    /* Lifecycle methods ----------------------------------------- */
    /* =========================================================== */

    @Override
    protected void onInit() {}

    @Override
    protected void onBeforeRendering(Request request, Response response) {
        if (isLogged()) {
            navTo(response, "home");
        }
    }

    @Override
    protected void onAfterRendering(Request request, Response response) {
        // Clean view model invalid password class
        getView().getModel().set("isValid", "").set("username", "");
    }

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
            onLogIn(request, response);
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
    private void onLogIn(Request request, Response response) {

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
        onAuthenticate(null);
        request.session().invalidate();
        navTo(response, "home");
    }


    private void onSuccessfulAuthentication(Request request, Response response) {
        navTo(response, "home");
    }

    private void onUnsuccessfulAuthenticaiton(Request request, Response response) {
        String username = request.queryParams("user");
        getView().getModel().set("isValid", "is-invalid").set("username", username);
    }

}
