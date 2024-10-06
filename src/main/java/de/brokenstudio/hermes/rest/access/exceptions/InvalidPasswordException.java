package de.brokenstudio.hermes.rest.access.exceptions;

public class InvalidPasswordException extends Exception {

    public InvalidPasswordException() {
        super("Invalid password");
    }
}
