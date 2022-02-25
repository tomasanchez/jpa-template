package com.jpa.core.security.userdetails;

import java.io.Serializable;

/**
 * A representation of an authority given to an user.
 */
public interface GrantedAuthority extends Serializable {

    /**
     * An authority must be represented by a string.
     * 
     * @return the granted authority
     */
    String getAuthority();
}
