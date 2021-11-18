package com.jpa.core.mvc.controller;

public enum ControllerInitialization {
    /**
     * Initializates with all endpoints.
     */
    FULL_CRUD, CRUD_NOT_ENGINE,
    /**
     * Initilizates with GET and POST endpoints.
     */
    GET_POST,
    /**
     * Initializates with all GET and POST endpoints
     */
    FULL_GET_POST,
    /**
     * Initializates with all GET and POST and also DELETE endpoints
     */
    GET_POST_DELETE,
    /**
     * Initializates only the GET endpoint
     */
    ONLY_GET,
}
