package io.codelex.flightplanner.service;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.dto.FlightDTO;

import java.util.List;
import java.util.Map;

public interface FlightService {
    void clearFlights();

    Flight addFlight(Flight flight);

    List<Airport> searchAirports(String search);

    void deleteFlight(Long id);

    Flight getFlightById(Long id);

    Map<String, Object> searchFlights(FlightDTO dto);

    Map<String, Object> mapToResponse(Flight flight);

}
