package de.brokenstudio.hermes.rest.access;

import io.javalin.security.RouteRole;

public enum Role implements RouteRole {

    ADMIN(3),DEV(2),USER(1),NOT_SECURED(0);

    public int level;

    Role(int level){
        this.level = level;
    }

}
