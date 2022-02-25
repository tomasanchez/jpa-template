package com.jpa.core.security.auth;

import com.jpa.core.security.crypto.PasswordEncoder;
import com.jpa.core.security.userdetails.UserDetails;
import com.jpa.core.security.userdetails.UserDetailsService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractUserAuthenticationProvider {

    private PasswordEncoder passwordEncoder;
    private UserDetailsService userDetailsService;

    /**
     * Allows subclasses to actually retrieve the UserDetails from an implementation-specific
     * location.
     * 
     * @param username the username to retrieve
     * @param authentication the authentication token
     * @return the user information never <code>null</code> - instead an exception must be thrown
     */
    protected abstract UserDetails retrieveUser(String username, Authentication authentication);

    /**
     * Creates a successful Authentication object.
     * 
     * @param principal that should be the principal in the returned object
     * @param authentication that was presented to the provider for validation
     * @param user that was loaded by the implementation
     * @return that was loaded by the implementation
     */
    protected abstract Authentication createSuccessAuthentication(Object principal,
            Authentication authentication, UserDetails user);
}
