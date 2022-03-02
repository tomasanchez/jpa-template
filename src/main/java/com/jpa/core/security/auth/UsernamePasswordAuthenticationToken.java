package com.jpa.core.security.auth;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import com.jpa.core.security.userdetails.GrantedAuthority;
import com.jpa.core.security.userdetails.UserDetails;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A generic implementation of a username and password authentication token.
 */
@Data
@AllArgsConstructor
public class UsernamePasswordAuthenticationToken implements Authentication {

    private static final long serialVersionUID = 1L;

    private final Object principal;

    private Object credentials;

    private boolean isAuthenticated;

    private final Collection<? extends GrantedAuthority> authorities;

    /**
     * Safe constructor as the {@link #isAuthenticated()} will return <code>false</code>
     * 
     * @param principal the principal username
     * @param credentials the password credentials
     */
    public UsernamePasswordAuthenticationToken(Object principal, Object credentials) {
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
        authorities = Collections.unmodifiableList(Collections.emptyList());
    }

    @Override
    public String getName() {

        if (this.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) this.getPrincipal()).getUsername();
        }

        if (this.getPrincipal() instanceof Principal) {
            return ((Principal) this.getPrincipal()).getName();
        }

        return (this.getPrincipal() == null) ? "" : this.getPrincipal().toString();
    }

}
