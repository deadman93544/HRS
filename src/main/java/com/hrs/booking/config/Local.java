package com.hrs.booking.config;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//Created Profiles for Runtime.
//This can be used to provide some custom implementation for different Environment, i.e. Dev, Local, Prod

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Profile("local")
public @interface Local {
}
