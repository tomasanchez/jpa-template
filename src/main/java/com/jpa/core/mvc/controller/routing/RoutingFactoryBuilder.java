package com.jpa.core.mvc.controller.routing;

import com.jpa.core.mvc.controller.Controller;
import spark.ResponseTransformer;
import spark.TemplateEngine;

public class RoutingFactoryBuilder {

    private Controller controller;
    private TemplateEngine engine;
    private ResponseTransformer responseTransformer;


    /**
     * Sets the <code>Controller</code> to build the route for.
     * 
     * @param controller to be routed.
     * @return a RoutingFactoryBuilder
     */
    public RoutingFactoryBuilder controller(Controller controller) {
        this.controller = controller;
        return this;
    }

    /**
     * Sets the <code>TemplateEngine</code> to be used.
     * 
     * @param engine for generating Template Views
     * @return a RoutingFactoryBuilder
     */
    public RoutingFactoryBuilder engine(TemplateEngine engine) {
        this.engine = engine;
        return this;
    }

    /**
     * Sets the <code>ResponseTransfomer</code> used.
     * 
     * @param responseTransformer for generation object responses
     * @return a RoutingFactoryBuilder
     */
    public RoutingFactoryBuilder responseTransformer(ResponseTransformer responseTransformer) {
        this.responseTransformer = responseTransformer;
        return this;
    }

    /**
     * Builds the corresponding <code>RoutingFactory</code>.
     * 
     * @return a new <code>RoutingFactory</code>.
     */
    public RoutingFactory build() {
        return new RoutingFactory(controller, engine, responseTransformer);
    }

}
