package com.jpa.core.security.filter;

import java.util.Collection;
import com.jpa.core.security.auth.exception.ForbiddenException;
import com.jpa.core.security.auth.exception.UnauthorizedException;
import com.jpa.core.security.userdetails.GrantedAuthority;
import spark.Request;
import spark.Response;

/**
 * Generic Interface for authorization filter in Spark.
 * 
 * @author Tomás Sánchez
 */
public interface SparkAuthorizationFilter {

    /**
     * Session Authorization filter.
     * 
     * @param request the Spark HTTP request object
     * @param response the Spark HTTP response object
     * @param authorities a list of required authorities
     * @throws UnauthorizedException when no session was found or token has expired.
     * @throws ForbiddenException when current user is not authorized
     */
    public void authorizationFilter(Request request, Response response,
            Collection<GrantedAuthority> authorities);

    /**
     * 
     * Session Authorization filter.
     * 
     * @param request the Spark HTTP request object
     * @param response the Spark HTTP response object
     * @throws UnauthorizedException when no session was found or token has expired.
     * @throws ForbiddenException when current user is not authorized
     */
    public void authorizationFilter(Request request, Response response);
}
