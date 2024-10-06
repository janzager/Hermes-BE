package de.brokenstudio.hermes.rest.access;

import de.brokenstudio.hermes.app.Application;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.UUID;

public class AccessHandler {

    public boolean hasAccess(Context ctx){
        try {
            if(ctx.routeRoles().contains(Role.NOT_SECURED))
                return true;
            UUID token = UUID.fromString(ctx.header("Authorization").split(" ")[1]);
            if(!Application.app().getSessionHandler().checkToken(token))
                return false;
            Role role = Application.app().getRoleHandler().getRole(Application.app().getSessionHandler().getUsername(token));
            if(role == null)
                return false;
            if(new ArrayList<>(ctx.routeRoles()).get(0) == null)
                return false;
            return ((Role) new ArrayList<>(ctx.routeRoles()).get(0)).level <= role.level;

        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }

}
