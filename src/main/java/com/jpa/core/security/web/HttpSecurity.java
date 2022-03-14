package com.jpa.core.security.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import com.google.common.net.HttpHeaders;
import com.jpa.core.security.auth.AuthenticationManager;
import com.jpa.core.security.auth.AuthorizationManager;
import com.jpa.core.security.auth.SecurityContext;
import com.jpa.core.security.filter.SparkAuthorizationFilter;
import org.eclipse.jetty.http.HttpStatus;
import lombok.Getter;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;

@Getter
public class HttpSecurity implements SecurityContext {

    private AuthenticationManager authenticationManager;

    private AuthorizationManager authorizationManager;

    private final HashMap<String, String> corsHeaders = new HashMap<>();

    private final Collection<Filter> beforeFilters = new ArrayList<>();

    private boolean cors;

    private boolean csrf;

    private SparkAuthorizationFilter authorizationFilter;

    public HttpSecurity() {}

    /**
     * Generates a new HttpSecurity instance.
     * 
     * @param authenticationManager which will authenticate.
     * @param authorizationManager which will authorize.
     * @param userDetailsService which provides user
     */
    public HttpSecurity(AuthenticationManager authenticationManager,
            AuthorizationManager authorizationManager) {
        this.authenticationManager = authenticationManager;
        this.authorizationManager = authorizationManager;
    }

    /**
     * Adds a filter before the authentication
     * 
     * @param filter
     * @return
     */
    public HttpSecurity addFilterBefore(Filter filter) {
        this.beforeFilters.add(filter);
        return this;
    }

    public HttpSecurity cors() {
        this.cors = true;
        return this;
    }

    public HttpSecurity csrf() {
        this.csrf = true;
        return this;
    }

    public HttpSecurity authorizationFilter(SparkAuthorizationFilter filter) {
        this.authorizationFilter = filter;
        return this;
    }

    /* =========================================================== */
    /* Instance methods ------------------------------------------ */
    /* =========================================================== */

    /**
     * Sets the corresponding filter to Spark.
     */
    public void build() {

        if (this.cors) {
            putCorsHeaders();
            Spark.after(this::corsFilter);
        }

        if (this.csrf) {
            Spark.before(this::csrfFilter);
        }

        this.beforeFilters.forEach(Spark::before);
    }


    /* =========================================================== */
    /* Internal security filters --------------------------------- */
    /* =========================================================== */

    private void putCorsHeaders() {
        corsHeaders.put(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET,PUT,POST,DELETE,OPTIONS");
        corsHeaders.put(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        corsHeaders.put(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
                "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
        corsHeaders.put(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
    }

    private void corsFilter(Request request, Response response) {
        this.corsHeaders.forEach(response::header);
    }

    private void csrfFilter(Request request, Response response) {

        List<String> safeMethods = Arrays.asList("GET", "OPTIONS", "HEAD");

        if (!safeMethods.contains(request.requestMethod())) {
            if (!request.headers().contains(HttpHeaders.X_REQUESTED_WITH)) {
                Spark.halt(HttpStatus.BAD_REQUEST_400);
            }
        }
    }

}
