package de.brokenstudio.hermes.rest.access.exceptions;

public class NoUserFoundException extends Exception {

    public NoUserFoundException() {
        super("No user found");
    }
}
