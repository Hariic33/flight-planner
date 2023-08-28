package io.codelex.flightplanner.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException() {
        return ResponseEntity.badRequest().body("Invalid JSON");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        Map<String, String> validationErrors = new HashMap<>();
        for (FieldError fieldError : result.getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(validationErrors);
    }
}