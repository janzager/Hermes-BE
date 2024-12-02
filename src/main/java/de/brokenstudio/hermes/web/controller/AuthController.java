package de.brokenstudio.hermes.web.controller;

import de.brokenstudio.hermes.app.Application;
import de.brokenstudio.hermes.rest.access.Role;
import de.brokenstudio.hermes.rest.access.exceptions.InvalidPasswordException;
import de.brokenstudio.hermes.rest.access.exceptions.NoUserFoundException;
import de.brokenstudio.hermes.rest.access.exceptions.SamePasswordException;
import de.brokenstudio.hermes.rest.access.exceptions.UserAlreadyExistsException;
import de.brokenstudio.hermes.rest.annotations.Controller;
import de.brokenstudio.hermes.rest.annotations.Post;
import de.brokenstudio.hermes.web.dto.ChangePasswordDTO;
import de.brokenstudio.hermes.web.dto.UserDTO;
import io.javalin.http.Context;

import java.util.UUID;

@Controller("/user")
public class AuthController {

    @Post("/create")
    public void createUser(Context ctx) {
        UserDTO userDTO = ctx.bodyAsClass(UserDTO.class);
        if (userDTO.getUsername() == null || userDTO.getPassword() == null || userDTO.getUsername().isEmpty() || userDTO.getPassword().isEmpty()) {
            ctx.status(400).result("Missing required fields: username or password");
            return;
        }
        try {
            Application.app().getAuthHandler().createUser(userDTO.getUsername(), userDTO.getPassword());
            ctx.status(201).result("User created");
        } catch (UserAlreadyExistsException e) {
            ctx.status(409).result("User already exists");
        }
    }

    @Post("/login")
    public void login(Context ctx) {
        UserDTO userDTO = ctx.bodyAsClass(UserDTO.class);
        if (userDTO.getUsername() == null || userDTO.getPassword() == null || userDTO.getUsername().isEmpty() || userDTO.getPassword().isEmpty()) {
            ctx.status(400).result("Missing required fields: username or password");
            return;
        }
        boolean login = Application.app().getAuthHandler().login(userDTO.getUsername(), userDTO.getPassword());
        if (!login) {
            ctx.status(401).result("Invalid credentials");
            return;
        }
        UUID token = Application.app().getSessionHandler().getToken(userDTO.getUsername());
        ctx.status(200).result(token.toString());
    }

    @Post(value = "/change-password", access = Role.USER)
    public void changePassword(Context ctx) {
        ChangePasswordDTO dto = ctx.bodyAsClass(ChangePasswordDTO.class);
        if (dto.getUsername() == null || dto.getOldPassword() == null || dto.getNewPassword() == null || dto.getUsername().isEmpty() || dto.getOldPassword().isEmpty() || dto.getNewPassword().isEmpty()) {
            ctx.status(400).result("Missing required fields: username, oldPassword or newPassword");
            return;
        }

        UUID token = UUID.fromString(ctx.header("Authorization").split(" ")[1]);
        try {
            if(Application.app().getAuthHandler().changePassword(token, dto.getOldPassword(), dto.getNewPassword())){
                Application.app().getSessionHandler().invalidateAllTokens(dto.getUsername());
                UUID newToken = Application.app().getSessionHandler().getToken(dto.getUsername());
                ctx.status(200).result(newToken.toString());
            }else{

            }
        } catch (NoUserFoundException e) {
            ctx.status(404).result("User not found");
        } catch (SamePasswordException e) {
            ctx.status(400).result("New password is the same as the old password");
        } catch (InvalidPasswordException e) {
            ctx.status(400).result("Invalid password");
        }
    }
}
