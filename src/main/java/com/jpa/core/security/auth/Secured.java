package com.jpa.core.security.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Secured {

    /**
     * Allow access only to given roles or authorities. When no authorities are set, allows access
     * only when session is authenticated.
     * 
     * <br>
     * </br>
     * 
     * Recommended to use a role hirearchy to better organization.
     */
    String[] roles() default {};
}
