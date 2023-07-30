package io.codelex.flightplanner;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.dto.FlightDTO;
import io.codelex.flightplanner.repository.FlightInMemoryRepository;
import io.codelex.flightplanner.service.FlightInMemoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FlightInMemoryServiceTest {

    private FlightInMemoryService flightService;
    private FlightInMemoryRepository flightRepository;

    @BeforeEach
    public void setUp() {
        flightRepository = mock(FlightInMemoryRepository.class);
        flightService = new FlightInMemoryService(flightRepository);
    }

    @Test
    public void testSearchFlights() {
        String from = "RIX";
        String to = "ARN";
        LocalDate departureDate = LocalDate.now();
        List<Flight> flights = new ArrayList<>();
        Flight flight1 = new Flight(
                new Airport("Latvia", "Riga", "RIX"),
                new Airport("Sweden", "Stockholm", "ARN"),
                "Ryanair",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2)
        );
        Flight flight2 = new Flight(
                new Airport("Russia", "Moscow", "DME"),
                new Airport("United Arab Emirates", "Dubai", "DXB"),
                "Turkish Airlines",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(5)
        );
        flights.add(flight1);
        flights.add(flight2);

        when(flightRepository.getFlights()).thenReturn(flights);

        FlightDTO dto = new FlightDTO(from, to, departureDate);
        Map<String, Object> result = flightService.searchFlights(dto);

        assertNotNull(result);
        assertTrue(result.containsKey("items"));
        assertTrue(result.containsKey("page"));
        assertTrue(result.containsKey("totalItems"));

        List<Flight> foundFlights = extractFlightsFromResult(result);
        assertEquals(1, foundFlights.size());

        Flight foundFlight = foundFlights.get(0);
        assertEquals(from, foundFlight.getFrom().getAirport());
        assertEquals(to, foundFlight.getTo().getAirport());
        assertEquals(departureDate, foundFlight.getDepartureTime().toLocalDate());
    }

    @SuppressWarnings("unchecked")
    private List<Flight> extractFlightsFromResult(Map<String, Object> result) {
        return (List<Flight>) result.get("items");
    }
}