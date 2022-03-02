package com.jpa.config;

import com.jpa.core.config.WebSecurityConfig;
import com.jpa.core.mvc.controller.Controller;
import com.jpa.core.security.auth.AuthenticationManager;
import com.jpa.core.security.auth.AuthorizationManager;
import com.jpa.core.security.crypto.BCryptPasswordEncoder;
import com.jpa.core.security.crypto.PasswordEncoder;
import com.jpa.core.security.userdetails.UserDetailsService;
import com.jpa.security.auth.SimpleAuthenticationProvider;
import com.jpa.security.services.SimpleUserService;
import lombok.Getter;
import lombok.Setter;

/**
 * A Security Configuration.
 */
@Getter
@Setter
public final class SecurityConfig implements WebSecurityConfig {


    private AuthenticationManager authenticationManager;

    private AuthorizationManager authorizationManager;

    private UserDetailsService userDetailsService;

    private static SecurityConfig instance;

    public static SecurityConfig getInstance() {
        if (instance == null) {
            configure();
        }
        return instance;
    }

    private SecurityConfig() {}


    public static void configure() {
        instance = new SecurityConfig();
        instance.setUserDetailsService(new SimpleUserService());
        instance.setAuthenticationManager(instance.authProvider());
        Controller.setWebSecurityConfig(instance);
    }

    @Override
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Generates an Authentication Provider.
     * 
     * @return a Simple Authentication Provider with the current <code>PasswordEncoder</code> and
     *         <code>UserDetailsService</code>
     */
    private SimpleAuthenticationProvider authProvider() {
        SimpleAuthenticationProvider authProvider = new SimpleAuthenticationProvider();
        authProvider.setPasswordEncoder(getPasswordEncoder());
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }

}
