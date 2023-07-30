package io.codelex.flightplanner.controller;

import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/admin-api")
public class AdminApiController {
    private final FlightService flightService;

    public AdminApiController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PutMapping("/flights")
    @ResponseStatus(HttpStatus.CREATED)
    public Flight addFlight(@RequestBody @Valid Flight flight) {
        return flightService.addFlight(flight);
    }

    @DeleteMapping("/flights/{id}")
    public boolean deleteFlight(@PathVariable Long id) {
        return flightService.deleteFlight(id);
    }

    @GetMapping("/flights/{id}")
    public Flight fetchFlight(@PathVariable Long id) {
        return flightService.getFlightById(id);
    }
}