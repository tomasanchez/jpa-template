package com.jpa.core.config;

import com.jpa.core.security.auth.AuthenticationManager;
import com.jpa.core.security.auth.AuthorizationManager;
import com.jpa.core.security.crypto.PasswordEncoder;

public interface WebSecurityConfig {

    /**
     * Indicates the Authentication Manager used.
     * 
     * @return the current <code>AuthenticationManager</code>
     */
    public AuthenticationManager getAuthenticationManager();

    /**
     * Indicates the Authorization Manager used.
     * 
     * @return the <code>AuthorizationManager</code> configurated to be used
     */
    public AuthorizationManager getAuthorizationManager();

    /**
     * Retrieves the configurated password encoder.
     * 
     * @return a new password encoder of the configurated type
     */
    public PasswordEncoder getPasswordEncoder();
}
