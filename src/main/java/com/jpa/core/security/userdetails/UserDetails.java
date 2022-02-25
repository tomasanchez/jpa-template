package com.jpa.core.security.userdetails;

import java.io.Serializable;
import java.util.Collection;

/**
 * A user interface inspired by the Spring Security.
 */
public interface UserDetails extends Serializable {

    /**
     * Returns the authorities granted to the user. Cannot return <code>null</code>.
     * 
     * @return the authorities, sorted by natural key (never <code>null</code>)
     */
    Collection<? extends GrantedAuthority> getAuthorities();

    /**
     * Returns the password used to authenticate the user.
     * 
     * @return the password
     */
    String getPassword();

    /**
     * Returns the username used to authenticate the user. Cannot return <code>null</code>.
     * 
     * @return the username (never <code>null</code>)
     */
    String getUsername();

    /**
     * Indicates whether the user's account has expired. An expired account cannot be authenticated.
     * 
     * @return <code>true</code> if the user's account is valid (ie non-expired), <code>false</code>
     *         if no longer valid (ie expired)
     */
    boolean isAccountNonExpired();

    /**
     * Indicates whether the user is locked or unlocked. A locked user cannot be authenticated.
     * 
     * @return <code>true</code> if the user is not locked, <code>false</code> otherwise
     */
    boolean isAccountNonLocked();

    /**
     * Indicates whether the user's credentials (password) has expired. Expired credentials prevent
     * authentication.
     * 
     * @return <code>true</code> if the user's credentials are valid (ie non-expired),
     *         <code>false</code> if no longer valid (ie expired)
     */
    boolean isCredentialsNonExpired();

    /**
     * Indicates whether the user is enabled or disabled. A disabled user cannot be authenticated.
     * 
     * @return <code>true</code> if the user is enabled, <code>false</code> otherwise
     */
    boolean isEnabled();
}
