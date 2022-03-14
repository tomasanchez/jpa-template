package com.jpa.security.filter;

import java.util.Collection;
import java.util.Collections;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.jpa.core.security.auth.Authentication;
import com.jpa.core.security.auth.AuthorizationManager;
import com.jpa.core.security.auth.exception.ForbiddenException;
import com.jpa.core.security.auth.exception.UnauthorizedException;
import com.jpa.core.security.filter.SparkAuthorizationFilter;
import com.jpa.core.security.userdetails.GrantedAuthority;
import com.jpa.core.utils.JwtMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spark.Request;
import spark.Response;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SessionJwtAuthorizationFilter implements SparkAuthorizationFilter {

    private JwtMapper jwtMapper = new JwtMapper();

    private AuthorizationManager authorizationManager;

    @Override
    public void authorizationFilter(Request request, Response response,
            Collection<GrantedAuthority> authorities) {

        String jwt = request.session().attribute(Authentication.AUTHENTICATION_TOKEN_KEY);
        Authentication auth = null;

        // When no JWT means no authentication is provided.
        if (jwt == null) {
            throw new UnauthorizedException("No session was found");
        }

        // Verifies the JWT.
        try {

            auth = jwtMapper.retrieveUserAuthTokenFromHeader(jwt);
            request.session().attribute(Authentication.AUTHENTICATION_KEY, auth);

        } catch (TokenExpiredException e) {

            throw new UnauthorizedException(e.getMessage(), e);

        } catch (Exception e) {

            throw new ForbiddenException(e.getMessage(), e);

        }

        authorizationManager.authorize(auth, authorities);
    }

    @Override
    public void authorizationFilter(Request request, Response response) {
        authorizationFilter(request, response, Collections.emptyList());
    }

}
