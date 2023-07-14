package io.codelex.flightplanner.exception;

public class InvalidSearchException extends RuntimeException {
    public InvalidSearchException() {
        super("Invalid search criteria. 'from', 'to', and 'departureDate' parameters are required.");
    }
}