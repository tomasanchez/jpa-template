package com.jpa.controller;

import javax.persistence.EntityNotFoundException;
import com.jpa.core.security.auth.exception.ForbiddenException;
import com.jpa.core.security.auth.exception.UnauthorizedException;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Spark;

public final class GlobalExceptionHandler {

    static {
        Spark.exception(UnauthorizedException.class, GlobalExceptionHandler::onUnauthorized);
        Spark.exception(ForbiddenException.class, GlobalExceptionHandler::onForbidden);
        Spark.exception(EntityNotFoundException.class, GlobalExceptionHandler::onNotFound);
    }

    /* =========================================================== */
    /* Authentication ------------------------------------------ */
    /* =========================================================== */

    /**
     * Handles UnauthorizedException, navigating to login page.
     * 
     * @param e the UnauthorizedException
     * @param request the Spark HTTP request object
     * @param response the Spark HTTP response object
     */
    private static void onUnauthorized(UnauthorizedException e, Request request,
            Response response) {
        BaseController.navTo(response, "login");
        response.status(HttpStatus.UNAUTHORIZED_401);
    }


    /**
     * Handles ForbiddenException, navigating to home page.
     * 
     * @param e the ForbiddenException
     * @param request the Spark HTTP request object
     * @param response the Spark HTTP response object
     */
    private static void onForbidden(ForbiddenException e, Request request, Response response) {
        BaseController.navTo(response, "home");
        response.status(HttpStatus.FORBIDDEN_403);
    }

    /* =========================================================== */
    /* Entity Requests ------------------------------------------- */
    /* =========================================================== */

    /**
     * Handles EntityNotFoundException, navigating to Not Found Page.
     * 
     * @param e the EntityNotFoundException
     * @param request the Spark HTTP request object
     * @param response the Spark HTTP response object
     */
    private static void onNotFound(EntityNotFoundException e, Request request, Response response) {
        BaseController.navTo(response, "/404");
    }

}
