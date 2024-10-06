package de.brokenstudio.hermes.config;

import de.brokenstudio.hermes.util.Json;

import java.io.*;

public class Config {

    public static class Application {

        private String host;
        private int port;

        public Application() {
        }

        public Application(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }
    }

    public static class Database {

        private String host;
        private int port;
        private String user;
        private String password;
        private boolean ssl;

        public Database() {
        }

        public Database(String host, int port, String user, String password, boolean ssl) {
            this.host = host;
            this.port = port;
            this.user = user;
            this.password = password;
            this.ssl = ssl;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        public String getUser() {
            return user;
        }

        public String getPassword() {
            return password;
        }

        public boolean isSsl() {
            return ssl;
        }
    }

    private Application application;
    private Database database;

    public Config() {
    }

    public Config(Application application, Database database) {
        this.application = application;
        this.database = database;
    }

    public Application getApplication() {
        return application;
    }

    public Database getDatabase() {
        return database;
    }

    public static void createDefaultConfig(){
        File file = new File("config.json");
        if(!file.exists()){
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write(Json.gson.toJson(new Config(
                        new Application("localhost",7070),
                        new Database("localhost",5432, "foo", "bar", false)
                )));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Config fromFile(){
        File file = new File("config.json");
        if(!file.exists()){
            System.err.println("The config file does not exist. Please restart the application, so the file can be generated.");
            System.exit(0);
            return null;
        }

        try {
            return Json.gson.fromJson(new FileReader(file), Config.class);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
