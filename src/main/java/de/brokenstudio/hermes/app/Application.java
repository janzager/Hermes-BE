package de.brokenstudio.hermes.app;

import de.brokenstudio.hermes.config.Config;
import de.brokenstudio.hermes.rest.RestManager;
import de.brokenstudio.hermes.rest.access.AuthHandler;
import de.brokenstudio.hermes.rest.access.Role;
import de.brokenstudio.hermes.rest.access.RoleHandler;
import de.brokenstudio.hermes.rest.access.SessionHandler;
import de.brokenstudio.hermes.rest.access.exceptions.UserAlreadyExistsException;
import lombok.Getter;

@Getter
public class Application {

    private static Application app;
    private Config config;
    private AuthHandler authHandler;
    private SessionHandler sessionHandler;
    private RoleHandler roleHandler;
    private RestManager restManager;

    private void init(String[] args){
        Config.createDefaultConfig();
        config = Config.fromFile();
        authHandler = new AuthHandler();
        sessionHandler = new SessionHandler();
        roleHandler = new RoleHandler();

        if(args.length == 3){
            if(args[0].equals("--createAdmin")){
                try {
                    authHandler.createUser(args[1], args[2]);
                    roleHandler.setRole(args[1], Role.ADMIN);
                    System.out.println("Admin user created.");
                } catch (UserAlreadyExistsException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        restManager = new RestManager();
    }

    public static void start(String[] args){
        app = new Application();
        app.init(args);
    }

    public static Application app(){
        return app;
    }

}
