package com.jpa.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jpa.core.security.auth.UsernamePasswordAuthenticationToken;
import com.jpa.core.security.userdetails.GrantedAuthority;
import com.jpa.core.security.userdetails.SimpleGrantedAuthority;
import com.jpa.core.security.userdetails.User;

/**
 * A genercis JSON Web Token encoder/decoder.
 * 
 * @author Tomás Sánchez
 */
public class JwtMapper {

    private static final String ROLES_KEY = "roles";

    private static final String CREDENTIALS_KEY = "credentials";

    private static final String TOKEN_DEFAULT_SECRET = "secret";

    /**
     * Generally a bearer token uses the prefix BEARER.
     */
    private static final String TOKEN_BEARER_PREFIX = "";

    private Algorithm algorithm;

    private JWTVerifier verifier;

    public JwtMapper() {
        this(TOKEN_DEFAULT_SECRET);
    }

    public JwtMapper(String tokenSecret) {
        algorithm = Algorithm.HMAC256(tokenSecret.getBytes());
        verifier = JWT.require(algorithm).build();
    }

    /**
     * Obtains an User Authentication Token from a header
     * 
     * @param authHeader the authentication header
     * @return the user-password-authentication token object.
     */
    public UsernamePasswordAuthenticationToken retrieveUserAuthTokenFromHeader(String authHeader) {
        return obtainUserAuthToken(decodeToken(obtainTokenFromHeader(authHeader)));
    }

    /**
     * Obtains an User Authentication Token from a Json Web Token.
     * 
     * @param jwt encoded to decoded.
     * @return the UsernamePasswordAuthenticationToken.
     */
    public UsernamePasswordAuthenticationToken retrieveUserAuthTokekenFromJWT(String jwt) {
        return obtainUserAuthToken(decodeToken(jwt));
    }

    /**
     * Generates an authentication token for an User.
     * 
     * @param user from springboot userdetails
     * @param expirationInMinutes the expiration time in minutes
     * @param issuer the issuer value
     * @param withClaims wheter to sign with claims
     * @return a generated token
     */
    public String generateTokenForUser(User user, int expirationInMinutes, String issuer,
            boolean withClaims) {

        Builder jwtBuilder = JWT.create().withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationInMinutes))
                .withIssuer(issuer).withClaim(CREDENTIALS_KEY, user.getPassword());

        return withClaims ? jwtBuilder.withClaim(ROLES_KEY, obtainAuthorities(user)).sign(algorithm)
                : jwtBuilder.sign(algorithm);
    }

    /**
     * Obtains a tokeken from the authentication header
     * 
     * @param authHeader the authentication header
     * @return the token withouth the prefix 'BEARER '
     */
    private String obtainTokenFromHeader(String authHeader) {
        return authHeader.substring(TOKEN_BEARER_PREFIX.length());
    }

    /**
     * Verifies a token.
     * 
     * @param token to be verified by the validator Algorithm
     * @return a Decoded Jason Web Token
     */
    private DecodedJWT decodeToken(String token) {
        return verifier.verify(token);
    }


    /**
     * Obtains the user auth token from a decoded Jason Web Token.
     * 
     * @param decodedJwt a decoded JSON web Token
     * @return
     */
    private static UsernamePasswordAuthenticationToken obtainUserAuthToken(DecodedJWT decodedJwt) {

        String username = decodedJwt.getSubject();
        String credentials = decodedJwt.getClaim(CREDENTIALS_KEY).asString();
        boolean isAuthenticated = true;
        String[] roles = decodedJwt.getClaim(ROLES_KEY).asArray(String.class);

        return new UsernamePasswordAuthenticationToken(username, credentials, isAuthenticated,
                listAuthorities(roles));
    }

    /**
     * List all authorities decoded.
     * 
     * @param roles decoded roles.
     * @return a collection of Granted Authorities
     */
    private static Collection<SimpleGrantedAuthority> listAuthorities(String[] roles) {

        if (Objects.isNull(roles)) {
            return Collections.emptyList();
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
        return authorities;
    }

    /**
     * Obtains an user authorities list.
     * 
     * @param user to authorize
     * @return a list of authorities.
     */
    private List<String> obtainAuthorities(User user) {
        return user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}
