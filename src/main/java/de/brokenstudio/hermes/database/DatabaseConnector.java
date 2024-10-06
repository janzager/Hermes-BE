package de.brokenstudio.hermes.database;

import de.brokenstudio.hermes.util.AppAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector implements AppAccessor {

    private static DatabaseConnector instance;

    private String conString;
    private String username;
    private String password;

    public DatabaseConnector(){
        String host = config().getDatabase().getHost();
        username = config().getDatabase().getUser();
        password = config().getDatabase().getPassword();
        String port = String.valueOf(config().getDatabase().getPort());
        String database = config().getDatabase().getDatabase();
        String useSSL = String.valueOf(config().getDatabase().isSsl());

        conString = "jdbc:postgresql://" + host + ":" + port + "/" + database + "?useSSL=" + useSSL;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(conString, username, password);
    }


    public static DatabaseConnector getInstance(){
        if(instance == null)
            instance = new DatabaseConnector();
        return instance;
    }

}
