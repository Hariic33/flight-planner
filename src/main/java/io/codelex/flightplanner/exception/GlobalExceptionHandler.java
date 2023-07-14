package io.codelex.flightplanner.exception;

import io.codelex.flightplanner.response.ErrorResponse;
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
            InvalidSearchException.class
    })

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleBadRequestExceptions(Exception ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FlightAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleFlightAlreadyExistsException(FlightAlreadyExistsException ex) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(FlightNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleFlightNotFoundException(FlightNotFoundException ex) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception ex, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(status).body(errorResponse);
    }
}