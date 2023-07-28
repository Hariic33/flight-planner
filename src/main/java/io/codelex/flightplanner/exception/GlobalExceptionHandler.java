package io.codelex.flightplanner.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FlightException.class)
    public ResponseEntity<String> handleFlightExceptions(FlightException ex) {
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