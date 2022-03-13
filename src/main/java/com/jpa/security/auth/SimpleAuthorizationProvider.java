package com.jpa.security.auth;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import com.jpa.core.security.auth.Authentication;
import com.jpa.core.security.auth.AuthorizationManager;
import com.jpa.core.security.auth.exception.AuthorizationException;
import com.jpa.core.security.auth.exception.ForbiddenException;
import com.jpa.core.security.auth.exception.UnauthorizedException;
import com.jpa.core.security.userdetails.GrantedAuthority;

public class SimpleAuthorizationProvider implements AuthorizationManager {


    @Override
    public void authorize(Authentication authentication, Collection<GrantedAuthority> authorities)
            throws AuthorizationException {

        if (authentication == null) {
            throw new UnauthorizedException("No authentication provided");
        }

        if (authorities == null || authorities.isEmpty()) {
            return;
        }

        Collection<? extends GrantedAuthority> authAuthorities = authentication.getAuthorities();

        List<String> strAuthorities = authorities.stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        boolean hasAnyAuthority = authAuthorities.stream().map(GrantedAuthority::getAuthority)
                .anyMatch(strAuthorities::contains);

        if (!hasAnyAuthority) {
            throw new ForbiddenException(String.format(
                    "The subject %s has no required authority. Must have any of {%s}",
                    authentication.getName(), strAuthorities.stream()
                            .reduce((a, b) -> String.format("%s, %s", a, b)).orElse("Undefined")));
        }

    }

    @Override
    public void authorize(Authentication authentication) {
        authorize(authentication, Collections.emptyList());
    }

}
