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

    @Autowired
    AdminApiController adminApiController;

    @Autowired
    TestingApiController testingApiController;

    @Autowired
    FlightInMemoryRepository flightInMemoryRepository;

    @BeforeEach
    void setUp() {
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

        assertThrows(FlightAlreadyExistsException.class, () -> adminApiController.addFlight(request));

        List<Flight> flightsAfterAdding = flightInMemoryRepository.getFlights();
        assertEquals(0, flightsAfterAdding.size());
    }


    @Test
    void canClearFlight() {
        Flight flight1 = new Flight(new Airport("Latvia", "Riga", "RIX"),
                new Airport("Sweden", "Stockholm", "ARN"), "Ryanair",
                LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        Flight flight2 = new Flight(new Airport("Russia", "Moscow", "DME"),
                new Airport("United Arab Emirates", "Dubai", "DXB"), "Turkish Airlines",
                LocalDateTime.now(), LocalDateTime.now().plusHours(5));

        adminApiController.addFlight(flight1);
        adminApiController.addFlight(flight2);

        testingApiController.clearFlights();

        List<Flight> flightsAfterClearing = flightInMemoryRepository.getFlights();
        assertEquals(0, flightsAfterClearing.size());
    }
}