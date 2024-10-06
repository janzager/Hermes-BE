package de.brokenstudio.hermes.app;

import de.brokenstudio.hermes.config.Config;
import lombok.Getter;

public class Application {

    private static Application app;
    @Getter
    private Config config;

    private void init(){
        Config.createDefaultConfig();
        config = Config.fromFile();
    }

    public static void start(){
        app = new Application();
        app.init();
    }

    public static Application app(){
        return app;
    }

}
