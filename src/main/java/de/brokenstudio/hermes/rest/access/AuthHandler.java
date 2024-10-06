package de.brokenstudio.hermes.rest.access;

import de.brokenstudio.hermes.database.DatabaseConnector;
import de.brokenstudio.hermes.rest.access.exceptions.InvalidPasswordException;
import de.brokenstudio.hermes.rest.access.exceptions.NoUserFoundException;
import de.brokenstudio.hermes.rest.access.exceptions.SamePasswordException;
import de.brokenstudio.hermes.rest.access.exceptions.UserAlreadyExistsException;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

public class AuthHandler {

    public AuthHandler(){
        setupDatabase();
    }

    private void setupDatabase(){
        try(Connection con = DatabaseConnector.getInstance().getConnection()) {
            con.createStatement().execute("CREATE TABLE IF NOT EXISTS users (username VARCHAR PRIMARY KEY , password bytea, salt bytea, role VARCHAR)");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createUser(String username, String password) throws UserAlreadyExistsException {
        try(Connection con = DatabaseConnector.getInstance().getConnection()) {

            PreparedStatement ps = con.prepareStatement("SELECT 1 FROM users WHERE username=?");
            ps.setString(1, username);
            if(ps.executeQuery().next()){
                throw new UserAlreadyExistsException();
            }

            byte[] salt = generateSalt16Byte();
            byte[] hashedPassword = hashPassword(password, salt);

            PreparedStatement insertStatement = con.prepareStatement("INSERT INTO users (username, password, salt, role) VALUES (?, ?, ?, ?)");
            insertStatement.setString(1, username);
            insertStatement.setBytes(2, hashedPassword);
            insertStatement.setBytes(3, salt);
            insertStatement.setString(4, Role.USER.name());
            insertStatement.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean login(String username, String password){
        try(Connection con = DatabaseConnector.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT password, salt FROM users WHERE username=?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                byte[] salt = rs.getBytes("salt");
                byte[] hashedPassword = hashPassword(password, salt);
                byte[] storedPassword = rs.getBytes("password");
                return Arrays.equals(hashedPassword, storedPassword);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean changePassword(UUID token, String oldPassword, String newPassword) throws NoUserFoundException, SamePasswordException, InvalidPasswordException {
        String username = SessionHandler.getInstance().getUsername(token);
        try(Connection connection = DatabaseConnector.getInstance().getConnection()){
            PreparedStatement statement = connection.prepareStatement("SELECT password, salt FROM users WHERE username=?");
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if(!rs.next())
                throw new NoUserFoundException();
            byte[] oldHashed = hashPassword(oldPassword, rs.getBytes("salt"));
            if(Arrays.equals(oldHashed, rs.getBytes("password")))
                throw new InvalidPasswordException();
            byte[] newHashedOld = hashPassword(newPassword, rs.getBytes("salt"));
            if(!Arrays.equals(oldHashed, newHashedOld))
                throw new SamePasswordException();
            byte[] newSalt = generateSalt16Byte();
            byte[] newHashed = hashPassword(newPassword, newSalt);
            statement = connection.prepareStatement("UPDATE users SET password = ?, salt = ? WHERE username = ?");
            statement.setBytes(1, newHashed);
            statement.setBytes(2, newSalt);
            statement.setString(3, username);
            statement.execute();
            return true;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    private byte[] generateSalt16Byte() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);

        return salt;
    }

    public byte[] hashPassword(String password, byte[] salt){
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .withIterations(2)
                .withMemoryAsKB(66536)
                .withParallelism(1)
                .withSalt(salt);
        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(builder.build());
        byte[] result = new byte[32];
        generator.generateBytes(password.getBytes(StandardCharsets.UTF_8), result, 0, result.length);
        return result;
    }

}
