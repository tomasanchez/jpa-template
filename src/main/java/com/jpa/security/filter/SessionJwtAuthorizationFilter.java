package com.jpa.security.filter;

import java.util.Collection;
import com.jpa.core.security.auth.Authentication;
import com.jpa.core.security.auth.AuthorizationManager;
import com.jpa.core.security.auth.exception.AuthorizationException;
import com.jpa.core.security.userdetails.GrantedAuthority;
import com.jpa.core.utils.JwtMapper;
import org.eclipse.jetty.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import spark.Request;
import spark.Response;
import spark.Spark;

@Setter
@Getter
@AllArgsConstructor
public class SessionJwtAuthorizationFilter {

    private JwtMapper jwtMapper = new JwtMapper();
    private AuthorizationManager authorizationManager;

    protected void sessionAuthorize(Request request, Response response,
            Collection<GrantedAuthority> authorities) {

        String jwt = request.session().attribute(Authentication.AUTHENTICATION_TOKEN_KEY);
        Authentication auth = null;

        // When no JWT means no authentication is provided.
        if (jwt == null) {
            halt(request, HttpStatus.UNAUTHORIZED_401,
                    "This resource requires to be authenticated.");
            return;
        }

        // Verifies the JWT.
        try {

            auth = jwtMapper.retrieveUserAuthTokenFromHeader(jwt);

            request.session().attribute(Authentication.AUTHENTICATION_KEY, auth);

        } catch (Exception e) {

            halt(request, HttpStatus.FORBIDDEN_403, e.getMessage());
        }


        try {
            authorizationManager.authorize(auth, authorities);
        } catch (AuthorizationException e) {
            halt(request, HttpStatus.FORBIDDEN_403, e.getMessage());
        }
    }

    /**
     * Convenience method for halting.
     * 
     * @param request the Spark HTTP request object
     * @param statusCode the HTTP status code to be set
     * @param message to inform
     */
    private void halt(Request request, int statusCode, String message) {
        request.session().invalidate();
        Spark.halt(statusCode, message);
    }
}
