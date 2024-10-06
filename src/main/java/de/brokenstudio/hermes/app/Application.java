package de.brokenstudio.hermes.app;

public class Application {

    private static Application app;

    private void init(){

    }

    public static void start(){
        app = new Application();
        app.init();
    }

    public static Application app(){
        return app;
    }

}
