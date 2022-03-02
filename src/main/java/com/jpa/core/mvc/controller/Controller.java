package com.jpa.core.mvc.controller;

import static spark.Spark.after;
import static spark.Spark.before;
import java.lang.reflect.Method;
import java.util.Map;
import com.jpa.core.config.WebSecurityConfig;
import com.jpa.core.mvc.controller.routing.DeleteMapping;
import com.jpa.core.mvc.controller.routing.GetMapping;
import com.jpa.core.mvc.controller.routing.PostMapping;
import com.jpa.core.mvc.controller.routing.PutMapping;
import com.jpa.core.mvc.controller.routing.RoutingFactory;
import com.jpa.core.mvc.controller.routing.RoutingFactoryBuilder;
import com.jpa.core.mvc.model.Model;
import com.jpa.core.mvc.view.View;
import com.jpa.core.utils.JsonTransformer;
import com.jpa.i18n.ResourceBundle;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.ResponseTransformer;
import spark.TemplateEngine;

/**
 * A generic controller implementation for the TS-JPA Model-View-Controller concept.
 * 
 * @author Tomás Sánchez
 * @since 1.0
 */
public abstract class Controller {

    private static final String ENDPOINTS_MODEL_NAME = "links";
    private static final String NAV_MODEL_NAME = "nav";
    private static Model sharedModel;
    private static TemplateEngine engine;
    private static ResourceBundle i18n = new ResourceBundle();
    private static ResponseTransformer jsonTransformer = new JsonTransformer();
    private static WebSecurityConfig webSecurityConfig;
    private View view;

    /* =========================================================== */
    /* Construction ---------------------------------------------- */
    /* =========================================================== */

    public Controller() {
        setView(new View(getName()));
        onStartSharedModel();
        before(getEndPoint(), this::onBeforeBeforeRendering);
        onInitEndpoints();
        onInit();
        after(getEndPoint(), this::onAfterRendering);
    }

    /**
     * Initialices the shared model.
     */
    static {
        setSharedModel(new Model()).set(NAV_MODEL_NAME, new Model())
                .set(ENDPOINTS_MODEL_NAME, new Model())
                .set("i18n", new Model(getI18n().getProperties()));
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

    public static ResourceBundle getI18n() {
        return i18n;
    }

    /**
     * Retrieves the current JSON transformer.
     * 
     * @return the JSON response transformer
     */
    public static ResponseTransformer getJsonTransformer() {
        return jsonTransformer;
    }

    /**
     * Sets a JSON response transformer to be used by the controllers.
     * 
     * @param responseTransformer the JSON response transformer.
     */
    public static void setJsonTransformer(ResponseTransformer responseTransformer) {
        jsonTransformer = responseTransformer;
    }

    /**
     * Sets the Web Security Configuration to be used by the controllers.
     * 
     * @param wsc a Web Security Configuration to be used
     */
    public static void setWebSecurityConfig(WebSecurityConfig wsc) {
        webSecurityConfig = wsc;
    }

    protected static WebSecurityConfig getWebSecurityConfig() {
        return webSecurityConfig;
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
        return "/".concat(getName()).toLowerCase();
    }

    /**
     * Generates a Model and View.
     * 
     * @return a Model and View
     */
    public ModelAndView getModelAndView() {
        return new ModelAndView(getModelMap(), getView().getPath());
    }

    /**
     * Generates a Model and View.
     * 
     * @param path the view file path
     * @return a Model and View
     */
    public ModelAndView getModelAndView(String path) {
        return new ModelAndView(getModelMap(), path);
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
     * This method is called every time a view is rendered, is shared between all the controllers.
     * 
     * @param request the Spark HTTP request object
     * @param response the Spark HTTP response object
     */
    protected void onBeforeBeforeRendering(Request request, Response response) {
        // Updates locales.
        getSharedModel().set("i18n",
                new Model(getI18n().setLang(request.headers("Accept-Language")).getProperties()));
        updateNavigationModel(getShortName());
        onBeforeRendering(request, response);
    }

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
    /* Internal methods ------------------------------------------ */
    /* =========================================================== */

    /**
     * 
     * Inits corresponding endpoints.
     */
    private void onInitEndpoints() {

        Method[] methods = getClass().getDeclaredMethods();


        RoutingFactory routingFactory = new RoutingFactoryBuilder().controller(this)
                .engine(getEngine()).responseTransformer(getJsonTransformer()).build();

        // CRUD HTTP Request Endpoints

        // Create
        routingFactory.createEndpointsFor(PostMapping.class, methods);

        // Read
        routingFactory.createEndpointsFor(GetMapping.class, methods);

        // Update
        routingFactory.createEndpointsFor(PutMapping.class, methods);

        // Delete
        routingFactory.createEndpointsFor(DeleteMapping.class, methods);

    }


    /**
     * Shares essential data for the application view model.
     */
    private void onStartSharedModel() {

        // Binds navigation for nav-links highlighting, set to "active" when in the current link.
        ((Model) getSharedModel().get(NAV_MODEL_NAME)).set(getShortName(), "");
        // Binds navigation #href links according to the controller's view.
        ((Model) getSharedModel().get(ENDPOINTS_MODEL_NAME)).set(getShortName(), getEndPoint());
    }

    /**
     * Shortcut method for obtainning the final Map model.
     * 
     * @return the final map model.
     */
    private Map<String, Object> getModelMap() {
        return getView().getModel().join(getSharedModel()).getData();
    }

    /**
     * Updates navigation active class.
     * 
     * @param currentView the current view name
     */
    protected void updateNavigationModel(String currentView) {
        ((Model) getSharedModel().get(NAV_MODEL_NAME))
                .replaceAll((k, v) -> v = k.equals(currentView) ? "active" : "");
    }

}
