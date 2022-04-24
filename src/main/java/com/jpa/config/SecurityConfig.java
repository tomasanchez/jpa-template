package com.jpa.config;

import com.jpa.core.config.Configuration;
import com.jpa.core.config.WebSecurityConfig;
import com.jpa.core.security.crypto.BCryptPasswordEncoder;
import com.jpa.core.security.crypto.PasswordEncoder;
import com.jpa.core.security.web.HttpSecurity;
import com.jpa.security.auth.SimpleAuthenticationProvider;
import com.jpa.security.auth.SimpleAuthorizationProvider;
import com.jpa.security.filter.SessionJwtAuthorizationFilter;
import com.jpa.security.services.SimpleUserService;
import lombok.Getter;
import lombok.Setter;

/**
 * A Security Configuration.
 */
@Getter
@Setter
@Configuration
public final class SecurityConfig extends WebSecurityConfig {

    @Override
    public void configure() {

        HttpSecurity http = new HttpSecurity(authenticationProvider(), authorizationProvider());

        SessionJwtAuthorizationFilter jwtAuthorizationFilter = new SessionJwtAuthorizationFilter();
        jwtAuthorizationFilter.setAuthorizationManager(http.getAuthorizationManager());

        http.cors().authorizationFilter(jwtAuthorizationFilter).build();

        setSecurityContext(http);
    }

    /**
     * Obtains the used password-encoder.
     * 
     * @return the used password-encoder
     */
    public static PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Generates an Authentication Provider.
     * 
     * @return a Simple Authentication Provider with the current <code>PasswordEncoder</code> and
     *         <code>UserDetailsService</code>
     */
    public static SimpleAuthenticationProvider authenticationProvider() {
        SimpleAuthenticationProvider authProvider = new SimpleAuthenticationProvider();
        authProvider.setPasswordEncoder(getPasswordEncoder());
        authProvider.setUserDetailsService(new SimpleUserService());
        return authProvider;
    }


    /**
     * Builds an Authorization Provider.
     * 
     * @return a Simple <code>AuthorizationProvider</code>.
     */
    public static SimpleAuthorizationProvider authorizationProvider() {
        return new SimpleAuthorizationProvider();
    }

}
