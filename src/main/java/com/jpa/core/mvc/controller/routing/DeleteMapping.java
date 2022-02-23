package com.jpa.core.mvc.controller.routing;

public @interface DeleteMapping {

    /**
     * The path of the endpoint.
     * 
     */
    String path() default "/:id";

}
