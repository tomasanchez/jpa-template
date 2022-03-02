package com.jpa.core.mvc.controller.routing;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import com.jpa.core.mvc.controller.Controller;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import spark.ModelAndView;
import spark.ResponseTransformer;
import spark.Route;
import spark.Spark;
import spark.TemplateEngine;
import spark.TemplateViewRoute;

@AllArgsConstructor
public class RoutingFactory {

    @NonNull
    private Controller controller;

    @NonNull
    private TemplateEngine engine;

    @NonNull
    private ResponseTransformer responseTransformer;


    /**
     * Generates the corresponding Request method for the given Annotation.
     * 
     * @param annotation a Request Mapping annotation
     * @param methods methods that will generate in that request
     */
    public void createEndpointsFor(Class<? extends Annotation> annotation, Method[] methods) {


        Arrays.stream(methods).filter(m -> m.isAnnotationPresent(annotation)).forEach(method -> {

            method.setAccessible(true);

            if (annotation.equals(GetMapping.class)) {
                createGetEndpoint(method);
                return;
            }

            if (annotation.equals(PostMapping.class)) {
                createPostEndpoint(method);
                return;
            }

            if (annotation.equals(PutMapping.class)) {
                createPutEndpoint(method);
                return;
            }

            if (annotation.equals(DeleteMapping.class)) {
                createDeleteEndpoint(method);
                return;
            }

        });

    }


    /**
     * Maps a method to a GET request.
     * 
     * @param method to handle a GET request
     */
    public void createGetEndpoint(Method method) {

        GetMapping annotation = method.getAnnotation(GetMapping.class);

        boolean useEngine = annotation.engine();
        String path = formatPath(annotation.path());

        if (useEngine) {
            Spark.get(path, routeViewMethod(method), this.engine);
        } else {
            Spark.get(path, routeMethod(method), this.responseTransformer);
        }

    }

    /**
     * Maps a method to a POST request.
     * 
     * @param method to handle a POST request
     */
    public void createPostEndpoint(Method method) {

        PostMapping annotation = method.getAnnotation(PostMapping.class);

        boolean useEngine = annotation.engine();
        String path = formatPath(annotation.path());

        if (useEngine) {
            Spark.post(path, routeViewMethod(method), this.engine);
        } else {
            Spark.post(path, routeMethod(method), this.responseTransformer);
        }

    }


    /**
     * Maps a method to a PUT request.
     * 
     * @param method to handle a PUT request
     */
    public void createPutEndpoint(Method method) {

        PutMapping annotation = method.getAnnotation(PutMapping.class);

        String path = formatPath(annotation.path());

        Spark.put(path, routeMethod(method), this.responseTransformer);

    }

    /**
     * Maps a method to a DELETE request.
     * 
     * @param method to handle a DELETE request
     */
    public void createDeleteEndpoint(Method method) {

        DeleteMapping annotation = method.getAnnotation(DeleteMapping.class);

        String path = formatPath(annotation.path());

        Spark.delete(path, routeMethod(method), this.responseTransformer);

    }


    /**
     * Generates and endpoint for the specified path.
     * 
     * @param path a path to use in the endpoint of a controller
     * @return the route with the final endpoint
     */
    private String formatPath(String path) {

        if (this.controller.getEndPoint().equals(path)) {
            return path;
        }

        path = path.startsWith("/") || path.isEmpty() ? this.controller.getEndPoint().concat(path)
                : String.format("%s/%s", this.controller.getEndPoint(), path);
        return path;
    }


    /**
     * Gets a Spark route from a method.
     * 
     * @param method to be routed
     * @return a Spark Java endpoint
     */
    private Route routeMethod(Method method) {
        return (request, response) -> method.invoke(this.controller, request, response);
    }


    /**
     * Gets a Spartk Template View Route from a method.
     * 
     * @param method to be tempalted-view routed.
     * @return a Spark Java view endpoint
     */
    private TemplateViewRoute routeViewMethod(Method method) {
        return (request,
                response) -> (ModelAndView) method.invoke(this.controller, request, response);
    }
}
