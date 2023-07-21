package io.codelex.flightplanner.controller;

import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.dto.FlightDTO;
import io.codelex.flightplanner.exception.FlightNotFoundException;
import io.codelex.flightplanner.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<FlightDTO> addFlight(@Valid @RequestBody Flight flight) {
        if (!flightService.validateFlightRequest(flight)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        FlightDTO flightDTO = flightService.mapToDTO(flight);
        flightService.addFlights(flightDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(flightDTO);
    }

    @DeleteMapping("/flights/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable String id) {
        try {
            flightService.deleteFlight(id);
            return ResponseEntity.ok().build();
        } catch (FlightNotFoundException e) {
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/flights/{id}")
    public ResponseEntity<FlightDTO> fetchFlight(@PathVariable String id) {
        try {
            FlightDTO flight = flightService.getFlightById(id);
            return ResponseEntity.ok(flight);
        } catch (FlightNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}