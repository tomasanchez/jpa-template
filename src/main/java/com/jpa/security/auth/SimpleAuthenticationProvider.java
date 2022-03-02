package com.jpa.security.auth;

import com.jpa.core.security.auth.AbstractUserAuthenticationProvider;
import com.jpa.core.security.auth.Authentication;
import com.jpa.core.security.auth.UsernamePasswordAuthenticationToken;
import com.jpa.core.security.userdetails.UserDetails;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleAuthenticationProvider extends AbstractUserAuthenticationProvider {

    @Override
    protected UserDetails retrieveUser(String username, Authentication authentication) {
        return getUserDetailsService().loadUserByUsername(username);
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal,
            Authentication authentication, UserDetails user) {
        String password = authentication.getCredentials().toString();
        String encodedPassword = getPasswordEncoder().encode(password);
        return new UsernamePasswordAuthenticationToken(user, encodedPassword, true,
                user.getAuthorities());
    }

}
