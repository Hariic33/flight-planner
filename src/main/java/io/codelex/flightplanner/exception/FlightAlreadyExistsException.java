package io.codelex.flightplanner.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class FlightAlreadyExistsException extends FlightException {
    public FlightAlreadyExistsException() {
        super("Flight already exists");
    }
}
