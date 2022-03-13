package com.jpa.core.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Simple Configuration bean annotation.
 * 
 * <br>
 * </br>
 * 
 * All classes mark with this annotattion should extend from <code>SimpleConfiguration</code> and
 * implement the <code>configure()/code> method.
 * 
 * @author Tomás Sánchez
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Configuration {

}
