package io.codelex.flightplanner.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FlightNotFoundException extends FlightException {
    public FlightNotFoundException() {
        super("Flight wasn't found");
    }
}