package com.jpa.controller;

import static spark.Spark.halt;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import com.jpa.core.mvc.controller.Controller;
import com.jpa.core.security.auth.Authentication;
import com.jpa.core.security.userdetails.GrantedAuthority;
import com.jpa.core.services.ControllerLoaderService;
import com.jpa.core.utils.JwtMapper;
import com.jpa.model.user.Privilege;
import com.jpa.model.user.User;
import com.jpa.repositories.UserRepository;
import com.jpa.services.UserService;
import spark.Request;
import spark.Response;


/**
 * Foundational controller.
 * 
 * <br>
 * </br>
 * 
 * Common and inheritable methods for all other controllers, including: Model methods and shortcuts;
 * Router, History handling and Navigation.
 * 
 * @author Tomás Sánchez
 */
public abstract class BaseController extends Controller {

    protected static GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    protected static final Map<String, User> authenticationMap = new HashMap<>();

    /* =========================================================== */
    /* LifeCycle Override Methods -------------------------------- */
    /* =========================================================== */

    @Override
    protected void onBeforeBeforeRendering(Request request, Response response) {
        super.onBeforeBeforeRendering(request, response);

        // When match on Log-In do not update the view model.
        if (request.matchedPath()
                .equals(ControllerLoaderService.getService().find("login").getEndPoint())) {
            return;
        }

        // This is used for logged in session.
        getSharedModel().set("loggedIn", isLogged(request));
        getSharedModel().set("user", onRetrieveUser(retrieveAuthentication(request).orElse(null)));
        setCorrespondingViewLinks(request);
    }

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
    public static String getText(String text) {
        return (String) Controller.getI18n().getText(text);
    }

    /**
     * Navigates to the specified location.
     * 
     * @param response the spark HTTP response object
     * @param location an endpoint, a controller name or a path
     */
    public static void navTo(Response response, String location) {

        if (location.startsWith("/")) {
            response.redirect(location);
            return;
        }

        Controller c = ControllerLoaderService.getService().find(location);
        response.redirect(Objects.isNull(c) ? "/".concat(location) : c.getEndPoint());

    }

    /**
     * Extracted method for responding 501.
     * 
     * @param response the http response object
     * @return an unimplemented method
     */
    protected Object unImplementedMethod(Response response) {
        response.status(501);
        return response;
    }

    /* =========================================================== */
    /* Authentification ------------------------------------------ */
    /* =========================================================== */

    /**
     * Sets User into the shared model.
     * 
     * @param user the user to be set
     */
    protected static void modelAuthenticate(User user) {
        getSharedModel().set("user", user).set("loggedIn", !Objects.isNull(user));
        setCorrespondingViewLinks(user);
    }

    /**
     * Sets the shared model for link visibility.
     * 
     * @param user the user to verify authorities against
     */
    protected static void setCorrespondingViewLinks(User user) {
        boolean isAdmin = hasAuthority(user, "ROLE_ADMIN");
        boolean isStaff = hasAuthority(user, "ROLE_STAFF");
        getSharedModel().set("isAdmin", isAdmin).set("isStaff", isStaff);
    }

    /**
     * Sets the shared model for link visibility.
     * 
     * @param request the HTTP Spark request object from where to obtain session
     */
    protected static void setCorrespondingViewLinks(Request request) {
        boolean isAdmin = hasAuthority(request, "ROLE_ADMIN");
        boolean isStaff = hasAuthority(request, "ROLE_STAFF");
        getSharedModel().set("isAdmin", isAdmin).set("isStaff", isStaff);
    }

    /**
     * Retrieves a model User from the request object.
     * 
     * @param request the HTTP Spark request object
     * @return an optional of user
     */
    protected static Optional<User> onRetrieveUser(Request request) {
        try {

            Authentication authentication =
                    retrieveAuthentication(request).orElseThrow(IllegalStateException::new);
            return new UserService().findByUsername(authentication.getName());

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    protected static User onRetrieveUser(Authentication authentication) {

        try {
            return authenticationMap.get(authentication.getPrincipal());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Reloads user data from the database.
     * 
     * @return the user updated.
     */
    protected User onRefreshUser() {
        User refreshed = new UserRepository()
                .getEntity(((User) getSharedModel().get("user")).getId()).orElse(null);
        getSharedModel().set("user", refreshed);
        return refreshed;
    }

    /**
     * Verifies if an user is loaded into the shared view model.
     * 
     * @return wheter there is an user or not
     */
    protected boolean isLogged() {
        return !Objects.isNull(getSharedModel().get("user"));
    }

    /**
     * Verifies if a session is stablished in the request.
     * 
     * @param request the Spark HTTP
     * @return wheter there is a session or not
     */
    protected static boolean isLogged(Request request) {

        try {

            retrieveAuthentication(request).orElseThrow(IllegalArgumentException::new);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifies if session is active in the request and model.
     * 
     * <br>
     * </br>
     * 
     * ? If not logged in, redirects to 401
     * 
     * @param request the Spark HTTP request object
     * @param response the Spark HTTP response object
     */
    protected void onRequireSession(Request request, Response response) {
        if (!isLogged(request) || !isLogged()) {
            halt(401);
            response.redirect(ControllerLoaderService.getService().find("login").getEndPoint(),
                    401);
        }
    }

    /**
     * Checks wether a session has a given authority.
     * 
     * @param request the HTTP Spark request object
     * @param authority the authority to check
     * @return wether the authentication has the authority or not
     */
    protected static boolean hasAuthority(Request request, String authority) {
        try {

            return retrieveAuthentication(request).orElseThrow(IllegalArgumentException::new)
                    .getAuthorities().stream().map(GrantedAuthority::getAuthority)
                    .anyMatch(grantedAuthority -> grantedAuthority.equals(authority));

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if an <code>User</code> has a given authority.
     * 
     * @param user to verify
     * @param authority required for the user to have
     * @return wether the authentication contains it or not.
     */
    protected static boolean hasAuthority(User user, String authority) {

        if (Objects.isNull(user)) {
            return false;
        }

        try {
            return user.getRole().getPrivileges().stream().map(Privilege::getName)
                    .anyMatch(privilege -> privilege.equals(authority));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Retrieves an Authentication from a Request object.
     * 
     * @param request the HTTP Spark request object
     * @return an Optional of Authentication
     */
    protected static Optional<Authentication> retrieveAuthentication(Request request) {

        try {
            String jwt = request.session().attribute(Authentication.AUTHENTICATION_TOKEN_KEY);
            return Optional.of(new JwtMapper().retrieveUserAuthTokekenFromJWT(jwt));
        } catch (Exception e) {
            return Optional.empty();
        }

    }

}
