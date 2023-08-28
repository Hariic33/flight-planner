package io.codelex.flightplanner;

import io.codelex.flightplanner.controller.AdminApiController;
import io.codelex.flightplanner.controller.TestingApiController;
import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.exception.FlightAlreadyExistsException;
import io.codelex.flightplanner.repository.FlightInMemoryRepository;
import io.codelex.flightplanner.service.FlightInMemoryService;
import io.codelex.flightplanner.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class FlightApplicationTests {
    AdminApiController adminApiController;
    TestingApiController testingApiController;
    FlightService flightService;

    @Autowired
    FlightInMemoryRepository flightInMemoryRepository;

    @BeforeEach
    void setUp() {
        flightService = new FlightInMemoryService(flightInMemoryRepository);
        adminApiController = new AdminApiController(flightService);
        testingApiController = new TestingApiController(flightService);
        flightInMemoryRepository.clearFlightList();
    }

    @Test
    void canAddFlight() {
        Airport from = new Airport("Latvia", "Riga", "RIX");
        Airport to = new Airport("Sweden", "Stockholm", "ARN");
        String carrier = "Ryanair";
        LocalDateTime departureTime = LocalDateTime.of(2023, 7, 13, 10, 0);
        LocalDateTime arrivalTime = LocalDateTime.of(2023, 7, 13, 11, 30);
        Flight request = new Flight(from, to, carrier, departureTime, arrivalTime);

        Flight addedFlight = adminApiController.addFlight(request);

        assertTrue(flightInMemoryRepository.getFlights().contains(addedFlight));

        assertThrows(FlightAlreadyExistsException.class, () -> adminApiController.addFlight(request));
    }


    @Test
    void canClearFlight() {
        Flight flight = new Flight(new Airport("Latvia", "Riga", "RIX"),
                new Airport("Sweden", "Stockholm", "ARN"), "Ryanair",
                LocalDateTime.now(), LocalDateTime.now().plusHours(2));

        adminApiController.addFlight(flight);

        testingApiController.clearFlights();

        List<Flight> flightsAfterClearing = flightInMemoryRepository.getFlights();
        assertEquals(0, flightsAfterClearing.size());
    }
}