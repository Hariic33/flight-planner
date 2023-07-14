package io.codelex.flightplanner.controller;

import io.codelex.flightplanner.domain.FlightPlanner;
import io.codelex.flightplanner.dto.FlightPlannerDTO;
import io.codelex.flightplanner.exception.FlightNotFoundException;
import io.codelex.flightplanner.service.FlightPlannerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/admin-api")
public class AdminApiController {
    private final FlightPlannerService flightPlannerService;

    public AdminApiController(FlightPlannerService flightPlannerService) {
        this.flightPlannerService = flightPlannerService;
    }

    @PutMapping("/flights")
    public ResponseEntity<FlightPlannerDTO> addFlight(@Valid @RequestBody FlightPlanner flight) {
        if (!flightPlannerService.validateFlightRequest(flight)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        FlightPlannerDTO flightPlannerDTO = flightPlannerService.mapToDTO(flight);
        flightPlannerService.addFlights(flightPlannerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(flightPlannerDTO);
    }

    @DeleteMapping("/flights/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable String id) {
        try {
            flightPlannerService.deleteFlight(id);
            return ResponseEntity.ok().build();
        } catch (FlightNotFoundException e) {
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/flights/{id}")
    public ResponseEntity<FlightPlannerDTO> fetchFlight(@PathVariable String id) {
        try {
            FlightPlannerDTO flight = flightPlannerService.getFlightById(id);
            return ResponseEntity.ok(flight);
        } catch (FlightNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}