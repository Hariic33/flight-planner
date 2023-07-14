package io.codelex.flightplanner.controller;

import io.codelex.flightplanner.service.FlightPlannerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testing-api")
public class TestingApiController {
    private final FlightPlannerService flightPlannerService;

    public TestingApiController(FlightPlannerService flightPlannerService) {
        this.flightPlannerService = flightPlannerService;
    }

    @PostMapping("/clear")
    public ResponseEntity<Void> clearFlights() {
        flightPlannerService.clearFlights();
        return ResponseEntity.ok().build();
    }
}