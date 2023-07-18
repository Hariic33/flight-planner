package io.codelex.flightplanner;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.dto.FlightPlannerDTO;
import io.codelex.flightplanner.repository.FlightPlannerRepository;
import io.codelex.flightplanner.service.FlightPlannerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FlightPlannerServiceTest {
    @Mock
    private FlightPlannerRepository flightPlannerRepository;

    @InjectMocks
    private FlightPlannerService flightPlannerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void searchFlights_ShouldReturnMatchingFlights() {

        String from = "RIX";
        String to = "ARN";
        String departureDate = "2023-07-18";
        FlightPlannerDTO flight1 = new FlightPlannerDTO("1", new Airport("Latvia", "Riga", "RIX"),
                new Airport("Sweden", "Stockholm", "ARN"),
                "Ryanair", LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        FlightPlannerDTO flight2 = new FlightPlannerDTO("2", new Airport("Russia", "Moscow", "DME"),
                new Airport("United Arab Emirates", "Dubai", "DXB"), "Turkish Airlines",
                LocalDateTime.now(), LocalDateTime.now().plusHours(5));

        List<FlightPlannerDTO> flights = Arrays.asList(flight1, flight2);
        when(flightPlannerRepository.getFlights()).thenReturn(flights);

        List<FlightPlannerDTO> result = flightPlannerService.searchFlights(from, to, departureDate);

        assertEquals(1, result.size());
    }
}