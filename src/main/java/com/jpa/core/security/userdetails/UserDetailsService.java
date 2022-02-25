package com.jpa.core.security.userdetails;

import javax.persistence.EntityNotFoundException;

/**
 * User interface to retrieve user credentials. Inspired by spring-security.
 *
 */
public interface UserDetailsService {

    /**
     * Locates the user based on the username. In the actual implementation, the search may possibly
     * be case sensitive, or case insensitive depending on how the implementation instance is
     * configured. In this case, the <code>UserDetails</code> object that comes back may have a
     * username that is of a different case than what was actually requested.
     * 
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws EntityNotFoundException if the user could not be found or the user has no
     *         GrantedAuthorities
     */
    UserDetails loadUserByUsername(String username) throws EntityNotFoundException;
}
