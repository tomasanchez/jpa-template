package com.jpa.core.mvc.controller;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.patch;
import static spark.Spark.post;
import static spark.Spark.put;
import com.jpa.core.mvc.model.Model;
import com.jpa.core.mvc.view.View;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;

/**
 * A generic controller implementation for the TS-JPA Model-View-Controller concept.
 * 
 * @author Tomás Sánchez
 * @since 1.0
 */
public abstract class Controller {

    private static Model sharedModel;
    private static TemplateEngine engine;
    private static ControllerInitialization DEF_INIT = ControllerInitialization.ONLY_GET;
    private View view;

    /* =========================================================== */
    /* Construction ---------------------------------------------- */
    /* =========================================================== */

    public Controller() {
        setView(new View(getName()));
        onStartSharedModel();
        before(getEndPoint(), this::onBeforeRendering);
        onInitEndpoints();
        onInit();
        after(getEndPoint(), this::onAfterRendering);
    }

    /**
     * Initialices the shared model.
     */
    static {
        setSharedModel(new Model()).set("nav", new Model()).set("links", new Model());
    }

    /**
     * Override this method to change initilialization.
     * 
     * <br>
     * 
     * @return the controller initilialization
     */
    protected ControllerInitialization getInitialization() {
        return DEF_INIT;
    }

    /* =========================================================== */
    /* Getters & Setter ------------------------------------------ */
    /* =========================================================== */

    /**
     * Gets the shared model among controllers.
     * 
     * @return A shared model
     */
    public static Model getSharedModel() {
        return sharedModel;
    }

    /**
     * Sets a shared model among all controllers.
     * 
     * @param sharedModel a model to be shared between controllers
     * @return the shared model
     */
    public static Model setSharedModel(Model sharedModel) {
        Controller.sharedModel = sharedModel;
        return getSharedModel();
    }

    /**
     * Obtains the current engine used.
     * 
     * @return the spark template engine
     */
    public static TemplateEngine getEngine() {
        return engine;
    }

    /**
     * Sets the current engine to be used.
     * 
     * @param templateEngine a spark template engine
     */
    public static void setEngine(TemplateEngine templateEngine) {
        engine = templateEngine;
    }

    /**
     * Retrieves the current controller view.
     * 
     * @return the view of the current controller
     */
    public View getView() {
        return view;
    }

    /**
     * Sets a controller view.
     * 
     * @param view
     * @return
     */
    public Controller setView(View view) {
        this.view = view;
        return this;
    }

    /**
     * Obtains the controller name to lowercase.
     * 
     * ? Example: HomeController => home
     * 
     * @return the controller name
     */
    public String getShortName() {
        return getClass().getSimpleName().toLowerCase().replace("controller", "");
    }

    /**
     * Obtains the controller name.
     * 
     * ? Example: NewObjectController => NewObject
     * 
     * @return the controller name
     */
    public String getName() {
        return getClass().getSimpleName().replace("Controller", "");
    }

    /**
     * Retrieves the controller endpoint URL.
     * 
     * ? Example: HomeController => /home
     * 
     * @return the endpoint controlled
     */
    public String getEndPoint() {
        return getEndPoint(false);
    }

    /**
     * Retrieves the controller endpoint URL.
     * 
     * <br>
     * </br>
     * 
     * ? Example: HomeController => /home/:id (when using id)
     * 
     * @param useId if use id path
     * @return the endpoint controlled
     */
    public String getEndPoint(Boolean useId) {
        return useId ? "/".concat(getName()).concat("/:id") : "/".concat(getName());
    }

    /**
     * Generates a Model and View.
     * 
     * @return a Model and View
     */
    public ModelAndView getModelAndView() {
        return new ModelAndView(getView().getModel().join(getSharedModel()), getView().getPath());
    }

    /**
     * Generates a Model and View.
     * 
     * @param path the view file path
     * @return a Model and View
     */
    public ModelAndView getModelAndView(String path) {
        return new ModelAndView(getView().getModel().join(getSharedModel()), path);
    }

    /* =========================================================== */
    /* Lifecycle methods ----------------------------------------- */
    /* =========================================================== */

    /**
     * This method is called upon initialization of the View. The controller can perform its
     * internal setup in this hook. It is only called once per View instance, unlike the
     * onBeforeRendering and onAfterRendering hooks.
     */
    protected abstract void onInit();

    /**
     * 
     * This method is called every time the View is rendered, before the Renderer is called and the
     * HTML is placed in the DOM-Tree. It can be used to perform clean-up-tasks before re-rendering.
     * 
     * @param request the Spark HTTP request object
     * @param response the Spark HTTP response object
     */
    protected abstract void onBeforeRendering(Request request, Response response);

    /**
     * 
     * This method is called every time the View is rendered, after the HTML is placed in the
     * DOM-Tree. It can be used to apply additional changes to the Model after the Renderer has
     * finished.
     * 
     * @param request the spark HTTP request object
     * @param response the spark HTTP response object
     */
    protected abstract void onAfterRendering(Request request, Response response);

    /* =========================================================== */
    /* HTTP Request Handling ------------------------------------- */
    /* =========================================================== */

    /* ===================== */
    /* GET REQUEST --------- */
    /* ===================== */

    /**
     * Sets HTTP GET response.
     * 
     * @return the controller instance
     */
    public Controller setGet() {
        return setGet(false, false);
    }

    /**
     * Sets HTTP GET reponse.
     * 
     * @param useEngine flag to use or not a template engine
     * @return the controller instance
     */
    public Controller setGet(Boolean useEngine) {
        return setGet(useEngine, false);
    }

    /**
     * Sets HTTP GET reponse.
     * 
     * @param useEngine flag to use or not a template engine
     * @param useId flag to use or not an id endpoint
     * @return the controller instance
     */
    public Controller setGet(Boolean useEngine, Boolean useId) {

        String path = getEndPoint(useId);

        if (useEngine) {
            get(path, this::onGet, getEngine());
        } else {
            get(path, this::onGetResponse);
        }
        return this;
    }


    public Controller setGetFullCrud() {
        get(getEndPoint().concat("/new"), this::onGetNew);
        setGet(true, true);
        return this;
    }

    /**
     * Function called when an HTTP GET request is received.
     * 
     * @param request the Spark HTTP request object
     * @param response the Spark HTTP response object
     * @return an object
     */
    protected ModelAndView onGet(Request request, Response response) {
        return getModelAndView();
    }

    protected ModelAndView onGetNew(Request request, Response response) {
        return getModelAndView("New".concat(getView().getPath()));
    }

    /**
     * Function called when an HTTP GET request is received and NO Template Engine is used.
     * 
     * @param request the Spark HTTP request object
     * @param response the Spark HTTP response object
     * @return an object
     */
    protected Object onGetResponse(Request request, Response response) {
        return unImplementedMethod(response);
    }

    /* ===================== */
    /* POST REQUEST ======== */
    /* ===================== */

    /**
     * Sets the HTTP POST endpoint.
     * 
     * @return the controller instance
     */
    public Controller setPost() {
        return setPost(false);
    }

    /**
     * Sets the HTTP POST endpoint.
     * 
     * @param useEngine if uses a template engine
     * @return the controller instance
     */
    public Controller setPost(Boolean useEngine) {
        return setPost(useEngine, false);
    }

    /**
     * Sets the HTTP POST endpoint.
     * 
     * @param useEngine if uses a template engine
     * @param useId if uses id endpoint
     * @return the controller instance
     */
    public Controller setPost(Boolean useEngine, Boolean useId) {

        String path = getEndPoint(useId);

        if (useEngine) {
            post(path, this::onGet, getEngine());
        } else {
            post(path, this::onPostResponse);
        }

        return this;
    }

    /**
     * Function called when an HTTP POST request is received.
     * 
     * @param request the Spark HTTP request object
     * @param response the Spark HTTP response object
     * @return a Model and View
     */
    protected ModelAndView onPost(Request request, Response response) {
        return getModelAndView();
    }

    /**
     * Function called when an HTTP GET request is received an NO Template Engine is used.
     * 
     * @param request the Spark HTTP request object
     * @param response the Spark HTTP response object
     * @return an object
     */
    protected Object onPostResponse(Request request, Response response) {
        return unImplementedMethod(response);
    }

    /* ===================== */
    /* PUT REQUEST ======= */
    /* ===================== */

    /**
     * Sets the endpoit for HTTP PUT request;
     * 
     * @return the controller instance.
     */
    public Controller setPut() {
        put(getEndPoint(true), this::onPutResponse);
        return this;
    }

    /**
     * Function called when an HTTP PUT/PATCH request is received and NO Template Engine is used.
     * 
     * <br>
     * </br>
     * 
     * ? This should not return a Model and View
     * 
     * @param request the Spark HTTP request object
     * @param response the Spark HTTP response object
     * @return an object
     */
    protected Object onPutResponse(Request request, Response response) {
        return unImplementedMethod(response);
    }

    /* ===================== */
    /* PATCH REQUEST ======= */
    /* ===================== */

    /**
     * Sets the endpoit for HTTP PATCH request;
     * 
     * @return the controller instance.
     */
    public Controller setPatch() {
        patch(getEndPoint(true), this::onPutResponse);
        return this;
    }

    /* ===================== */
    /* DELETE REQUEST ====== */
    /* ===================== */

    /**
     * Sets the endpoit for HTTP DELETE request;
     * 
     * @return the controller instance.
     */
    public Controller setDelete() {
        delete(getEndPoint(true), this::onDeleteResponse);
        return this;
    }

    /**
     * Function called when an HTTP DELETE request is received and NO Template Engine is used.
     * 
     * <br>
     * </br>
     * 
     * ? This should not return a Model and View
     * 
     * @param request the Spark HTTP request object
     * @param response the Spark HTTP response object
     * @return an object
     */
    protected Object onDeleteResponse(Request request, Response response) {
        return unImplementedMethod(response);
    }


    /* =========================================================== */
    /* Internal methods ------------------------------------------ */
    /* =========================================================== */

    private void onInitEndpoints() {

        switch (getInitialization()) {
            case FULL_CRUD:
                setGetFullCrud();
                setPost(true);
                setPost(true, true);
                setDelete();
                setPatch();
                setPut();
                break;
            case CRUD_NOT_ENGINE:
                setGet(false, true);
                setGet();
                setPost(false, true);
                setPost();
                setDelete();
                setPatch();
                setPut();
            case GET_POST_DELETE:
                setGet(true);
                setGet(true, true);
                setPost(true);
                setPost(true, true);
                setDelete();
            case GET_POST:
                setGet(true);
                setPost(true);
                break;
            default:
                setGet(true);
                break;
        }
    }

    /**
     * Shares essential data for the application view model.
     */
    private void onStartSharedModel() {

        // Binds navigation for nav-links highlighting, set to "active" when in the current link.
        ((Model) getSharedModel().get("nav")).set(getShortName(), "");
        // Binds navigation #href links according to the controller's view.
        ((Model) getSharedModel().get("links")).set(getShortName(), getEndPoint());
    }

    /**
     * Extracted method for responding 501.
     * 
     * @param response the http response object
     * @return an unimplemented method
     */
    private Object unImplementedMethod(Response response) {
        response.status(501);
        return response;
    }
}
