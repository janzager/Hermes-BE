package de.brokenstudio.hermes.rest.access;

import de.brokenstudio.hermes.database.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class RoleHandler {

    private final HashMap<String, Role> userRoles;

    public RoleHandler(){
        userRoles = new HashMap<>();
    }

    public void loadRole(String username){
        try(Connection connection = DatabaseConnector.getInstance().getConnection()){
            PreparedStatement ps = connection.prepareStatement("SELECT role FROM users WHERE username=?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                userRoles.put(username, Role.valueOf(rs.getString("role")));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setRole(String username, Role role){
        userRoles.put(username, role);
        CompletableFuture.runAsync(() -> {
            try(Connection connection = DatabaseConnector.getInstance().getConnection()){
                PreparedStatement ps = connection.prepareStatement("UPDATE users SET role=? WHERE username=?");
                ps.setString(1, role.name());
                ps.setString(2, username);
                ps.execute();
            }catch (SQLException e){
                e.printStackTrace();
            }

        });
    }

    public Role getRole(String username){
        return userRoles.getOrDefault(username, null);
    }

}
