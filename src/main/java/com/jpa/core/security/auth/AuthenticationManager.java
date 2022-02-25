package com.jpa.core.security.auth;

import com.jpa.core.security.auth.exception.AuthenticationException;


/**
 * Process an {@link Authentication} request.
 *
 */
public interface AuthenticationManager {

    /**
     * Attempts to authenticate, returning a fully populated <code>Authentication</code> object.
     * 
     * @param authentication the authentication request object
     * @return a fully authenticated object including credentials
     * @throws AuthenticationException if authentication fails
     */
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException;

}
