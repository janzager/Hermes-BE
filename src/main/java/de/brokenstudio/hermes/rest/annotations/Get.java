package de.brokenstudio.hermes.rest.annotations;

import de.brokenstudio.hermes.rest.access.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Get {

    String value() default "";
    Role access() default Role.NOT_SECURED;

}
