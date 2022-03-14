package com.jpa.controller;

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
    }

    /* =========================================================== */
    /* Authentication ------------------------------------------ */
    /* =========================================================== */

    private static void onUnauthorized(UnauthorizedException e, Request request,
            Response response) {
        BaseController.navTo(response, "login");
        response.status(HttpStatus.UNAUTHORIZED_401);
    }


    private static void onForbidden(ForbiddenException e, Request request, Response response) {
        BaseController.navTo(response, "home");
        response.status(HttpStatus.FORBIDDEN_403);
    }

}
