package de.brokenstudio.hermes.rest.access;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SessionHandler {

    private static SessionHandler instance;

    private final HashMap<UUID, String> tokens;
    private final HashMap<UUID, Long> tokenLife;

    private SessionHandler(){
        tokens = new HashMap<>();
        tokenLife = new HashMap<>();
    }

    public UUID getToken(String userrname){
        UUID uuid = generateUUID();
        tokens.put(uuid, userrname);
        tokenLife.put(uuid, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));
        return uuid;
    }

    public boolean checkToken(String username, UUID uuid){
        if(isTokenValid(username, uuid)){
            tokenLife.put(uuid, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));
            return true;
        }
        return false;
    }

    private boolean isTokenValid(String username, UUID uuid){
        return tokens.containsKey(uuid) && tokens.get(uuid).equals(username) && tokenLife.get(uuid) > System.currentTimeMillis();
    }



    private UUID generateUUID(){
        UUID uuid = UUID.randomUUID();
        return tokens.containsKey(uuid) ? generateUUID() : uuid;
    }

    public static SessionHandler getInstance(){
        if(instance == null){
            instance = new SessionHandler();
        }
        return instance;
    }

}
