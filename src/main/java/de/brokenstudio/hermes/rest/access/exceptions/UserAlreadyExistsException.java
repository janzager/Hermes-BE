package de.brokenstudio.hermes.rest.access.exceptions;

public class UserAlreadyExistsException extends Exception {

    public UserAlreadyExistsException() {
        super("User already exists");
    }
}
