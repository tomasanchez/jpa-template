package com.jpa.controller;

import static spark.Spark.halt;

import java.util.Map;
import java.util.Objects;
import com.jpa.core.mvc.controller.Controller;
import com.jpa.core.services.ControllerLoaderService;
import com.jpa.model.user.User;
import com.jpa.repositories.UserRepository;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.Request;
import spark.Response;



public abstract class BaseController extends Controller
        implements WithGlobalEntityManager, TransactionalOps {


    /* =========================================================== */
    /* LifeCycle Static BaseController --------------------------- */
    /* =========================================================== */

    /**
     * Static method for initialization of BaseController.
     */
    public static void initBaseController() {
        onAuthenticate(null);
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
    public String getText(String text) {
        return (String) Controller.getI18n().getText(text);
    }

    /**
     * Navigates to the specified location.
     * 
     * @param response the spark HTTP response object
     * @param location an endpoint, a controller name or a path
     */
    public void navTo(Response response, String location) {
        if (location.startsWith("/")) {
            response.redirect(location);
            return;
        } else {
            Controller c = ControllerLoaderService.getService().find(location);
            if (Objects.isNull(c)) {
                response.redirect("/".concat(location));
                return;
            } else {
                response.redirect(c.getEndPoint());
                return;
            }
        }
    }
    /* =========================================================== */
    /* Authentification ------------------------------------------ */
    /* =========================================================== */

    /**
     * Sets User into the shared model.
     * 
     * @param user the user to be set
     */
    protected static void onAuthenticate(User user) {
        getSharedModel().set("user", user).set("loggedIn", !Objects.isNull(user));
    }

    /**
     * Reloads user data from the database.
     * 
     * @return the user updated.
     */
    protected User onRefreshUser() {
        User refreshed =
                new UserRepository().getEntity(((User) getSharedModel().get("user")).getId());
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
    protected boolean isLogged(Request request) {
        return !Objects.isNull(request.session().attribute("uid"));
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



}
