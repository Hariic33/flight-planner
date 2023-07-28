package io.codelex.flightplanner.controller;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.SearchFlightsRequest;
import io.codelex.flightplanner.dto.FlightDTO;
import io.codelex.flightplanner.exception.FlightException;
import io.codelex.flightplanner.exception.FlightNotFoundException;
import io.codelex.flightplanner.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public List<Airport> searchAirports(@RequestParam String search) {
        return flightService.searchAirports(search);
    }

    @PostMapping("/flights/search")
    public Map<String, Object> searchFlights(@RequestBody(required = false) @Valid SearchFlightsRequest request) {
        if (request == null || request.getFrom() == null || request.getTo() == null || request.getDepartureDate() == null) {
            throw new FlightException("Invalid search criteria");
        }

        List<FlightDTO> flights = flightService.searchFlights(request.getFrom(), request.getTo(), request.getDepartureDate());
        return Map.of(
                "page", 0,
                "totalItems", flights.size(),
                "items", flights
        );
    }

    @GetMapping("/flights/{id}")
    public FlightDTO getFlightById(@PathVariable String id) {
        try {
            return flightService.getFlightById(id);
        } catch (FlightNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}