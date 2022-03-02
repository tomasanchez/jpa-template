package com.jpa.core.security.filter;

import com.jpa.core.security.auth.Authentication;
import com.jpa.core.security.auth.AuthenticationManager;
import com.jpa.core.security.auth.UsernamePasswordAuthenticationToken;
import com.jpa.core.security.auth.exception.AuthenticationException;
import com.jpa.core.security.auth.exception.AuthenticationServiceException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spark.Request;
import spark.Response;

@Getter
@Setter
@NoArgsConstructor
public class UsernamePasswordAuthenticationFilter {

    public static final String USERNAME_FORM_KEY = "username";

    public static final String PASSWORD_FORM_KEY = "password";

    private boolean postOnly = true;

    private AuthenticationManager authenticationManager;

    /**
     * Creates a new authentication filter
     * 
     * @param authenticationManager
     */
    public UsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public Authentication attemptAuthentication(Request request, Response response)
            throws AuthenticationException {

        if (this.postOnly && !request.requestMethod().equals("POST")) {
            throw new AuthenticationServiceException(String.format(
                    "The Authentication method '%s' is not supported.", request.requestMethod()));
        }

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(username, password);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * Convenience method for retrieveing the password value from request.
     * 
     * @param request the Spark Java HTTP request object
     * @return the password value
     */
    protected String obtainPassword(Request request) {
        String password = request.queryParams("password");
        return (password != null) ? password : "";
    }

    /**
     * Convenience method for retrieveing username value from request.
     * 
     * @param request the Spark Java HTTP Request object.
     * @return the username
     */
    protected String obtainUsername(Request request) {
        String username = request.queryParams(USERNAME_FORM_KEY);
        return (username != null) ? username.trim() : "";
    }


}
