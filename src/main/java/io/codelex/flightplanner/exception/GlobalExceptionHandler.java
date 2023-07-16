package io.codelex.flightplanner.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            IllegalArgumentException.class,
            SameAirportsException.class,
            InvalidFlightDateException.class,
            InvalidSearchException.class,
            FlightAlreadyExistsException.class,
            FlightNotFoundException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleBadRequestExceptions(Exception ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (ex instanceof FlightAlreadyExistsException) {
            status = HttpStatus.CONFLICT;
        } else if (ex instanceof FlightNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        }

        String errorMessage = ex.getMessage();
        return ResponseEntity.status(status).body(errorMessage);
    }
}