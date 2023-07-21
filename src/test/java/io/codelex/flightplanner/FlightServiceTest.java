package io.codelex.flightplanner;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.dto.FlightDTO;
import io.codelex.flightplanner.repository.FlightRepository;
import io.codelex.flightplanner.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FlightServiceTest {
    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void searchFlights_ShouldReturnMatchingFlights() {
        String from = "RIX";
        String to = "ARN";
        String departureDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        FlightDTO flight1 = new FlightDTO(new Airport("Latvia", "Riga", "RIX"),
                new Airport("Sweden", "Stockholm", "ARN"),
                "Ryanair", LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        FlightDTO flight2 = new FlightDTO(new Airport("Russia", "Moscow", "DME"),
                new Airport("United Arab Emirates", "Dubai", "DXB"), "Turkish Airlines",
                LocalDateTime.now(), LocalDateTime.now().plusHours(5));

        List<FlightDTO> flights = Arrays.asList(flight1, flight2);
        when(flightRepository.getFlights()).thenReturn(flights);

        List<FlightDTO> result = flightService.searchFlights(from, to, departureDate);

        assertEquals(1, result.size());
    }
}