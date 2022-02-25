package com.jpa.core.security.auth;

import java.io.Serializable;
import java.security.Principal;
import java.util.Collection;
import com.jpa.core.security.userdetails.GrantedAuthority;

/**
 * Generic interface of an authentication token, which has been processed by the
 * {@link AuthenticationManager#authenticate(Authentication)}.
 */
public interface Authentication extends Principal, Serializable {

    /**
     * Indicates the authorities that the principal has been granted.
     * 
     * @return the authorities granted to the principal or an empty collection.
     */
    Collection<? extends GrantedAuthority> getAuthorities();

    /**
     * Usually a password, but could be anything relevant to the <code>AuthenticationManager</code>.
     * Callers are expected to populate the credentials.
     * 
     * @return the credentials that prove the identity of the <code>Principal</code>
     */
    Object getCredentials();

    /**
     * The identity of the principal being authenticated. In the case of an authentication request
     * with username and password, this would be the username.
     * 
     * @return the principal identity.
     */
    Object getPrincipal();

    /**
     * Indicates wether the authentication is validated or not.
     * 
     * @return true if the token has been authenticated
     */
    boolean isAuthenticated();
}
