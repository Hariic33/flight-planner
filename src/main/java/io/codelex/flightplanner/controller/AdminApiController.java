package io.codelex.flightplanner.controller;

import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.dto.FlightDTO;
import io.codelex.flightplanner.exception.FlightNotFoundException;
import io.codelex.flightplanner.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public FlightDTO addFlight(@Valid @RequestBody Flight flight) {
        if (!flightService.validateFlightRequest(flight)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        FlightDTO flightDTO = flightService.mapToDTO(flight);
        flightService.addFlights(flightDTO);
        return flightDTO;
    }

    @DeleteMapping("/flights/{id}")
    public void deleteFlight(@PathVariable String id) {
        try {
            flightService.deleteFlight(id);
        } catch (FlightNotFoundException ignored) {
        }
    }

    @GetMapping("/flights/{id}")
    public FlightDTO fetchFlight(@PathVariable String id) {
        try {
            return flightService.getFlightById(id);
        } catch (FlightNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}