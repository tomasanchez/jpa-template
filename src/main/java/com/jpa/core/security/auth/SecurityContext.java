package com.jpa.core.security.auth;

import com.jpa.core.security.filter.SparkAuthorizationFilter;

/**
 * 
 * Generic Interface for authentication/authorization management.
 * 
 * @author Tomás Sánchez
 */
public interface SecurityContext {

    /**
     * Retrieves the authentication manager associated.
     * 
     * @return an authentication manager.
     */
    public AuthenticationManager getAuthenticationManager();

    /**
     * Retrieves the authorization manager associated.
     * 
     * @return an authorization manager.
     */
    public AuthorizationManager getAuthorizationManager();

    /**
     * Retrieves the current filter used for authorization.
     * 
     * @return an SparkAuthorizationFilter.
     */
    public SparkAuthorizationFilter getAuthorizationFilter();
}
