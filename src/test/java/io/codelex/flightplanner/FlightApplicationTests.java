package io.codelex.flightplanner;

import io.codelex.flightplanner.controller.AdminApiController;
import io.codelex.flightplanner.controller.TestingApiController;
import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.dto.FlightDTO;
import io.codelex.flightplanner.repository.FlightRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FlightApplicationTests {

    @Autowired
    AdminApiController adminApiController;

    @Autowired
    TestingApiController testingApiController;

    @Autowired
    FlightRepository flightRepository;

    @Test
    void canAddFlight() {
        Airport from = new Airport("Latvia", "Riga", "RIX");
        Airport to = new Airport("Sweden", "Stockholm", "ARN");
        String carrier = "Ryanair";
        LocalDateTime departureTime = LocalDateTime.of(2023, 7, 13, 10, 0);
        LocalDateTime arrivalTime = LocalDateTime.of(2023, 7, 13, 11, 30);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Flight request = new Flight(from, to, carrier, formatter.format(departureTime), formatter.format(arrivalTime));

        adminApiController.addFlight(request);

        List<FlightDTO> flightsAfterAdding = flightRepository.getFlights();
        assertEquals(1, flightsAfterAdding.size());

        FlightDTO savedFlight = flightsAfterAdding.get(0);
        assertNotNull(savedFlight.getId());
        assertEquals(savedFlight.getFrom(), from);
        assertEquals(savedFlight.getTo(), to);
        assertEquals(savedFlight.getCarrier(), carrier);
        assertEquals(savedFlight.getDepartureTime(), departureTime);
        assertEquals(savedFlight.getArrivalTime(), arrivalTime);
    }

    @Test
    void canClearFlight() {
        FlightDTO flight1 = new FlightDTO(new Airport("Latvia", "Riga", "RIX"),
                new Airport("Sweden", "Stockholm", "ARN"), "Ryanair", LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        FlightDTO flight2 = new FlightDTO(new Airport("Russia", "Moscow", "DME"),
                new Airport("United Arab Emirates", "Dubai", "DXB"), "Turkish Airlines", LocalDateTime.now(), LocalDateTime.now().plusHours(5));
        flightRepository.addFlights(flight1);
        flightRepository.addFlights(flight2);

        assertDoesNotThrow(() -> testingApiController.clearFlights());

        List<FlightDTO> flightsAfterClearing = flightRepository.getFlights();
        assertEquals(0, flightsAfterClearing.size());
    }
}