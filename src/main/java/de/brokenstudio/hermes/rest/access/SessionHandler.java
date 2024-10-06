package de.brokenstudio.hermes.rest.access;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SessionHandler {

    private final HashMap<UUID, String> tokens;
    private final HashMap<UUID, Long> tokenLife;

    public SessionHandler(){
        tokens = new HashMap<>();
        tokenLife = new HashMap<>();
    }

    public UUID getToken(String username){
        UUID uuid = generateUUID();
        tokens.put(uuid, username);
        tokenLife.put(uuid, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));
        return uuid;
    }

    public boolean checkToken(UUID uuid){
        if(isTokenValid(uuid)){
            tokenLife.put(uuid, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));
            return true;
        }
        return false;
    }

    private boolean isTokenValid(UUID uuid){
        return tokens.containsKey(uuid) && tokenLife.get(uuid) > System.currentTimeMillis();
    }

    public String getUsername(UUID uuid){
        return tokens.get(uuid);
    }



    private UUID generateUUID(){
        UUID uuid = UUID.randomUUID();
        return tokens.containsKey(uuid) ? generateUUID() : uuid;
    }
}
