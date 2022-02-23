package com.jpa.core.mvc.controller.routing;

/**
 * Annotation for mapping HTTP PUT requests onto specific handler methods.
 */
public @interface PutMapping {

    /**
     * The path of the endpoint.
     * 
     */
    String path() default "/:id";

}
