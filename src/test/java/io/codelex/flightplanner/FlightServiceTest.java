package io.codelex.flightplanner;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.dto.FlightDTO;
import io.codelex.flightplanner.repository.FlightInMemoryRepository;
import io.codelex.flightplanner.service.FlightInMemoryService;
import io.codelex.flightplanner.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FlightServiceTest {

    @Mock
    private FlightInMemoryRepository repository;
    private FlightService flightService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        flightService = new FlightInMemoryService(repository);
    }

    @Test
    void testSearchFlights() {
        FlightDTO dto = new FlightDTO("RIX", "ARN", LocalDate.of(2023, 8, 10));
        Flight flight = createTestFlight();
        when(repository.getFlights()).thenReturn(Collections.singletonList(flight));

        Map<String, Object> result = flightService.searchFlights(dto);
        List<Flight> foundFlights = (List<Flight>) result.get("items");
        Flight foundFlight = foundFlights.get(0);

        assertEquals(1, foundFlights.size());
        assertTestFlightEquals(flight, foundFlight);
        verify(repository, times(1)).getFlights();
    }

    private Flight createTestFlight() {
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setFrom(new Airport("RIX", "Riga", "Latvia"));
        flight.setTo(new Airport("ARN", "Stockholm", "Sweden"));
        flight.setDepartureTime(LocalDateTime.of(2023, 8, 10, 12, 0));
        flight.setArrivalTime(LocalDateTime.of(2023, 8, 10, 14, 0));
        return flight;
    }

    private void assertTestFlightEquals(Flight expected, Flight actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getFrom(), actual.getFrom());
        assertEquals(expected.getTo(), actual.getTo());
        assertEquals(expected.getDepartureTime(), actual.getDepartureTime());
        assertEquals(expected.getArrivalTime(), actual.getArrivalTime());
    }
}