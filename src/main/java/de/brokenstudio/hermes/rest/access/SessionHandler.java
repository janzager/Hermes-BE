package de.brokenstudio.hermes.rest.access;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SessionHandler {

    private final HashMap<UUID, String> tokens;
    private final HashMap<UUID, Long> tokenLife;
    private final HashMap<String, List<UUID>> tokenByName;

    public SessionHandler(){
        tokens = new HashMap<>();
        tokenLife = new HashMap<>();
        tokenByName = new HashMap<>();
    }

    public UUID getToken(String username){
        UUID uuid = generateUUID();
        tokens.put(uuid, username);
        tokenLife.put(uuid, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));
        tokenByName.putIfAbsent(username, new ArrayList<>());
        tokenByName.get(username).add(uuid);
        return uuid;
    }

    public List<UUID> getAllTokens(String username){
        return tokenByName.getOrDefault(username, List.of());
    }

    public void invalidateAllTokens(String username){
        List<UUID> allTokensOfUser = getAllTokens(username);
        allTokensOfUser.forEach(cr -> {
            tokens.remove(cr);
            tokenLife.remove(cr);
        });
        allTokensOfUser.clear();
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
