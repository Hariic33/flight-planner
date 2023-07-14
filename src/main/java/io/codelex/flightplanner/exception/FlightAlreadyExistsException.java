package io.codelex.flightplanner.exception;

public class FlightAlreadyExistsException extends RuntimeException {
    public FlightAlreadyExistsException() {
        super("Flight already exists");
    }
}
