package de.brokenstudio.hermes.rest.access.exceptions;

public class SamePasswordException extends Exception {

    public SamePasswordException() {
        super("Old and new password are the same");
    }
}
