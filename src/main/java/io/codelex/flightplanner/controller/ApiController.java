package io.codelex.flightplanner.controller;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.dto.FlightDTO;
import io.codelex.flightplanner.service.FlightService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final FlightService flightService;

    public ApiController(FlightService flightInMemoryService) {
        this.flightService = flightInMemoryService;
    }

    @GetMapping("/airports")
    public List<Airport> searchAirports(@RequestParam String search) {
        return flightService.searchAirports(search);
    }

    @PostMapping("/flights/search")
    public Map<String, Object> searchFlights(@RequestBody FlightDTO flightDTO) {
        return flightService.searchFlights(flightDTO);
    }

    @GetMapping("/flights/{id}")
    public Map<String, Object> getFlightById(@PathVariable Long id) {
        Flight flight = flightService.getFlightById(id);
        return flightService.mapToResponse(flight);
    }
}