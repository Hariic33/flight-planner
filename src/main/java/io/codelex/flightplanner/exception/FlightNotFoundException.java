package io.codelex.flightplanner.exception;

public class FlightNotFoundException extends RuntimeException{
    public FlightNotFoundException() {
        super("Flight wasn't found");
    }
}
