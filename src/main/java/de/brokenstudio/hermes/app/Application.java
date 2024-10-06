package de.brokenstudio.hermes.app;

import de.brokenstudio.hermes.config.Config;
import de.brokenstudio.hermes.rest.RestManager;
import de.brokenstudio.hermes.rest.access.AccessHandler;
import de.brokenstudio.hermes.rest.access.AuthHandler;
import de.brokenstudio.hermes.rest.access.RoleHandler;
import de.brokenstudio.hermes.rest.access.SessionHandler;
import lombok.Getter;

@Getter
public class Application {

    private static Application app;
    private Config config;
    private AuthHandler authHandler;
    private SessionHandler sessionHandler;
    private RoleHandler roleHandler;
    private RestManager restManager;

    private void init(){
        Config.createDefaultConfig();
        config = Config.fromFile();
        authHandler = new AuthHandler();
        sessionHandler = new SessionHandler();
        roleHandler = new RoleHandler();
        restManager = new RestManager();
    }

    public static void start(){
        app = new Application();
        app.init();
    }

    public static Application app(){
        return app;
    }

}
