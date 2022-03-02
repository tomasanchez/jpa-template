package com.jpa.core.web;

import spark.Request;
import spark.Response;

/**
 * A generic lambda for Spark HTTP void Request/Response handlers.
 * 
 * @author Tomás Sánchez
 */
@FunctionalInterface
public interface RequestResponseHandler {

    public void handle(Request request, Response response);

}
