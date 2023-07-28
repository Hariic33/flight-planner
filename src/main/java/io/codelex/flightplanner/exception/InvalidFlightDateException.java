package io.codelex.flightplanner.exception;

public class InvalidFlightDateException extends RuntimeException {
    public InvalidFlightDateException() {
        super("Invalid flight date");
    }
}