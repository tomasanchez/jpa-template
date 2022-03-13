package com.jpa.core.security.auth;

import java.util.Collection;
import com.jpa.core.security.auth.exception.AuthorizationException;
import com.jpa.core.security.userdetails.GrantedAuthority;

/**
 * Verifies an {@link Authentication} request.
 *
 */
public interface AuthorizationManager {

    /**
     * Attempts to authorize, halting the request if could not authorize.
     * 
     * @param authentication the authentication request object
     * @param authorities the authorites required for the authentication to have
     * @throws AuthorizationException
     */
    public void authorize(Authentication authentication, Collection<GrantedAuthority> authorities)
            throws AuthorizationException;


    /**
     * Attempts to authorize, halting the request if could not authorize.
     * 
     * @param authentication the authentication request object
     * @throws AuthorizationException
     */
    public void authorize(Authentication authentication) throws AuthorizationException;
}
