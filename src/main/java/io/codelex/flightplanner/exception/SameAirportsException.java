package io.codelex.flightplanner.exception;

public class SameAirportsException extends RuntimeException {
    public SameAirportsException() {
        super("Departure and arrival airports cannot be the same");
    }
}
