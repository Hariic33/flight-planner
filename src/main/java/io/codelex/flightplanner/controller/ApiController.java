package io.codelex.flightplanner.controller;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.SearchFlightsRequest;
import io.codelex.flightplanner.dto.FlightDTO;
import io.codelex.flightplanner.exception.FlightNotFoundException;
import io.codelex.flightplanner.exception.InvalidSearchException;
import io.codelex.flightplanner.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final FlightService flightService;

    public ApiController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/airports")
    public ResponseEntity<List<Airport>> searchAirports(@RequestParam String search) {
        List<Airport> airports = flightService.searchAirports(search);
        return ResponseEntity.ok(airports);
    }

    @PostMapping("/flights/search")
    public ResponseEntity<Map<String, Object>> searchFlights(@RequestBody(required = false) @Valid SearchFlightsRequest request) {
        if (request == null || request.getFrom() == null || request.getTo() == null || request.getDepartureDate() == null) {
            throw new InvalidSearchException();
        }

        List<FlightDTO> flights = flightService.searchFlights(request.getFrom(), request.getTo(), request.getDepartureDate());
        Map<String, Object> response = Map.of(
                "page", 0,
                "totalItems", flights.size(),
                "items", flights
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/flights/{id}")
    public ResponseEntity<FlightDTO> getFlightById(@PathVariable String id) {
        try {
            FlightDTO flight = flightService.getFlightById(id);
            return ResponseEntity.ok(flight);
        } catch (FlightNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}