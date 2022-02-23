package com.jpa.core.mvc.controller.routing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for mapping HTTP PUT requests onto specific handler methods.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PutMapping {

    /**
     * The path of the endpoint.
     * 
     */
    String path() default "/:id";

}
