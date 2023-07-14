package io.codelex.flightplanner;

import io.codelex.flightplanner.controller.AdminApiController;
import io.codelex.flightplanner.controller.TestingApiController;
import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.FlightPlanner;
import io.codelex.flightplanner.dto.FlightPlannerDTO;
import io.codelex.flightplanner.repository.FlightPlannerRepository;
import io.codelex.flightplanner.service.FlightPlannerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class FlightPlannerApplicationTests {

    @Autowired
    AdminApiController adminApiController;

    @Autowired
    TestingApiController testingApiController;

    @Autowired
    FlightPlannerRepository flightPlannerRepository;

    @Test
    void canAddFlight() {
        Airport from = new Airport("Latvia", "Riga", "RIX");
        Airport to = new Airport("Sweden", "Stockholm", "ARN");
        String carrier = "Ryanair";
        LocalDateTime departureTime = LocalDateTime.of(2023, 7, 13, 10, 0);
        LocalDateTime arrivalTime = LocalDateTime.of(2023, 7, 13, 11, 30);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        FlightPlanner request = new FlightPlanner(from, to, carrier, formatter.format(departureTime), formatter.format(arrivalTime));

        FlightPlannerDTO savedFlight = adminApiController.addFlight(request).getBody();

        assertNotNull(savedFlight.getId());
        assertEquals(savedFlight.getFrom(), from);
        assertEquals(savedFlight.getTo(), to);
        assertEquals(savedFlight.getCarrier(), carrier);
        assertEquals(savedFlight.getDepartureTime(), departureTime);
        assertEquals(savedFlight.getArrivalTime(), arrivalTime);
    }

    @Test
    void canClearFlight() {
        FlightPlannerDTO flight1 = new FlightPlannerDTO("1", new Airport("Latvia", "Riga", "RIX"),
                new Airport("Sweden", "Stockholm", "ARN"), "Ryanair", LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        FlightPlannerDTO flight2 = new FlightPlannerDTO("2", new Airport("Russia", "Moscow", "DME"),
                new Airport("United Arab Emirates", "Dubai", "DXB"), "Turkish Airlines", LocalDateTime.now(), LocalDateTime.now().plusHours(5));
        flightPlannerRepository.addFlights(flight1);
        flightPlannerRepository.addFlights(flight2);

        ResponseEntity<Void> response = testingApiController.clearFlights();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<FlightPlannerDTO> flightsAfterClearing = flightPlannerRepository.getFlights();
        assertEquals(0, flightsAfterClearing.size());
    }

    @Test
    void shouldReturnMatchingFlightsForSearchQuery() {
        FlightPlannerRepository flightPlannerRepositoryMock = mock(FlightPlannerRepository.class);
        FlightPlannerService flightPlannerServiceMock = new FlightPlannerService(flightPlannerRepositoryMock);

        String searchCountryText = "Latvia";
        String searchCityText = "Riga";
        String searchAirportText = "RIX";
        List<FlightPlannerDTO> expectedSearchResults = Arrays.asList(
                new FlightPlannerDTO("1", new Airport("Latvia", "Riga", "RIX"),
                        new Airport("Sweden", "Stockholm", "ARN"), "Ryanair", LocalDateTime.now(), LocalDateTime.now().plusHours(2)),
                new FlightPlannerDTO("2", new Airport("Russia", "Moscow", "DME"),
                        new Airport("United Arab Emirates", "Dubai", "DXB"), "Turkish Airlines", LocalDateTime.now(), LocalDateTime.now().plusHours(5))
        );

        when(flightPlannerRepositoryMock.searchFlights(anyString(), anyString(), anyString())).thenReturn(expectedSearchResults);

        List<FlightPlannerDTO> actualSearchResults = flightPlannerServiceMock.searchFlights(searchCountryText, searchCityText, searchAirportText);

        verify(flightPlannerRepositoryMock).searchFlights(eq(searchCountryText), eq(searchCityText), eq(searchAirportText));
        assertEquals(expectedSearchResults, actualSearchResults);
        assertEquals(expectedSearchResults.size(), actualSearchResults.size());
    }
}