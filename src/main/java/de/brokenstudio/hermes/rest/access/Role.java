package de.brokenstudio.hermes.rest.access;

import io.javalin.security.RouteRole;

public enum Role implements RouteRole {

    ADMIN,DEV,USER,NOT_SECURED;

}
