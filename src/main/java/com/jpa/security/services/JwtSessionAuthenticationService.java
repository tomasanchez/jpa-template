package com.jpa.security.services;

import java.util.Objects;
import com.jpa.core.security.auth.Authentication;
import com.jpa.core.security.auth.AuthenticationManager;
import com.jpa.core.security.auth.UsernamePasswordAuthenticationToken;
import com.jpa.core.security.auth.exception.AuthenticationException;
import com.jpa.core.security.userdetails.User;
import com.jpa.core.utils.JwtMapper;
import org.eclipse.jetty.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spark.Filter;
import spark.Request;
import spark.Response;

/**
 * 
 * A simple Spark Session JWT authentication service.
 * 
 * @author Tomás Sánchez
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtSessionAuthenticationService {

    private static final int TEN_MINUTES = 10 * 60 * 1_000;

    JwtMapper jwtMapper = new JwtMapper();

    AuthenticationManager authenticationManager;

    public JwtSessionAuthenticationService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Perform actual authentication.
     * 
     * @param request the Spark Java HTTP request object
     * @param response the Spark Java HTTP response object
     */
    public void attemptAuthentication(Request request, Response response) throws Exception {
        this.attemptAuthentication(request, response, (r, q) -> {
        }, (r, q) -> {
        });
    }

    public void attemptAuthentication(Request request, Response response, Filter onSuccess,
            Filter onFailure) throws Exception {

        String username = request.queryParams("user");
        String password = request.queryParams("password");

        if (Objects.isNull(username) || Objects.isNull(password)) {
            response.status(HttpStatus.BAD_REQUEST_400);
            return;
        }

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(username, password);

        try {
            successfulAuthentication(request, response, authenticationManager.authenticate(token));
            onSuccess.handle(request, response);
        } catch (AuthenticationException e) {
            unsuccessfulAuthentication(request, response, e);
            onFailure.handle(request, response);
        }

    }


    /**
     * Behaviour for successful authentication.
     * 
     * @param request the Spark Java HTTP request object
     * @param response the Spark Java HTTP response object
     * @param authentication the object returned from authenticate
     */
    private void successfulAuthentication(Request request, Response response,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        String issuer = request.uri();
        String sessionToken = jwtMapper.generateTokenForUser(user, TEN_MINUTES * 6, issuer, true);

        request.session().attribute(Authentication.AUTHENTICATION_TOKEN_KEY, sessionToken);
        request.session().attribute(Authentication.AUTHENTICATION_KEY, authentication);
        response.status(HttpStatus.OK_200);
    }

    /**
     * Behaviour for unsuccessful authentication.
     * 
     * @param request the Spark Java HTTP request object
     * @param response the Spark Java HTTP response object
     * @param failed the exception thrown
     */
    private void unsuccessfulAuthentication(Request request, Response response,
            AuthenticationException failed) {

        request.session().invalidate();
        response.status(HttpStatus.UNAUTHORIZED_401);
        response.header("Error", failed.getMessage());
    }

}
