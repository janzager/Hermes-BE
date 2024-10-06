package de.brokenstudio.hermes.app;

import de.brokenstudio.hermes.config.Config;
import de.brokenstudio.hermes.rest.RestManager;
import lombok.Getter;

@Getter
public class Application {

    private static Application app;
    private Config config;
    private RestManager restManager;

    private void init(){
        Config.createDefaultConfig();
        config = Config.fromFile();
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
