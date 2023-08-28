package io.codelex.flightplanner.service;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.dto.FlightDTO;
import io.codelex.flightplanner.exception.*;
import io.codelex.flightplanner.repository.FlightInMemoryRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

public class FlightInMemoryService implements FlightService {
    private final FlightInMemoryRepository flightInMemoryRepository;

    public FlightInMemoryService(FlightInMemoryRepository flightInMemoryRepository) {
        this.flightInMemoryRepository = flightInMemoryRepository;
    }

    public void clearFlights() {
        flightInMemoryRepository.clearFlightList();
    }

    public Flight addFlight(Flight flight) {
        validateFlight(flight);

        flightInMemoryRepository.addFlight(flight);
        return flight;
    }

    public void validateFlight(Flight flight) {
        Airport from = flight.getFrom();
        Airport to = flight.getTo();
        LocalDateTime departureTime = flight.getDepartureTime();
        LocalDateTime arrivalTime = flight.getArrivalTime();
        String carrier = flight.getCarrier();

        if (flightInMemoryRepository.getFlights().contains(flight)) {
            throw new FlightAlreadyExistsException();
        }

        if (from.getAirport().trim().equalsIgnoreCase(to.getAirport().trim())) {
            throw new FlightException("Departure and arrival airports cannot be the same");
        }

        if (!departureTime.isBefore(arrivalTime) ||
                Stream.of(from, to, departureTime, arrivalTime, carrier, from.getCountry(), from.getCity(), from.getAirport(),
                                to.getCountry(), to.getCity(), to.getAirport())
                        .anyMatch(value -> value == null || value.toString().trim().isEmpty())) {
            throw new FlightException("Invalid flight request");
        }
    }

    public List<Airport> searchAirports(String search) {
        String lowercaseSearch = search.toLowerCase().trim();
        return flightInMemoryRepository.getFlights().stream()
                .map(Flight::getFrom)
                .filter(airport -> airport.getAirport().toLowerCase().contains(lowercaseSearch)
                        || airport.getCity().toLowerCase().contains(lowercaseSearch)
                        || airport.getCountry().toLowerCase().contains(lowercaseSearch))
                .map(airport -> new Airport(airport.getAirport(), airport.getCity(), airport.getCountry()))
                .toList();
    }

    public void deleteFlight(Long id) {
        try {
            Flight flight = getFlightById(id);
            if (flight != null) {
                flightInMemoryRepository.deleteFlight(flight);
            }
        } catch (FlightNotFoundException ignored) {
        }
    }

    public Flight getFlightById(Long id) {
        Flight flight = flightInMemoryRepository.getFlightById(id);
        if (flight == null) {
            throw new FlightNotFoundException();
        }
        return flight;
    }

    public Map<String, Object> searchFlights(FlightDTO dto) {
        String airportFrom = dto.from();
        String airportTo = dto.to();
        LocalDate departureDate = dto.departureDate();

        List<Flight> flights = flightInMemoryRepository.getFlights().stream()
                .filter(flight -> flight.getDepartureTime().toLocalDate().equals(departureDate))
                .filter(flight -> flight.getFrom().getAirport().equals(airportFrom))
                .filter(flight -> flight.getTo().getAirport().equals(airportTo))
                .toList();

        if (airportFrom == null || airportTo == null || airportFrom.equals(airportTo)) {
            throw new FlightException("Invalid airports");
        }

        return Map.of("items", flights, "page", 0, "totalItems", flights.size());
    }

    public Map<String, Object> mapToResponse(Flight flight) {
        return Map.of(
                "id", flight.getId(),
                "from", flight.getFrom(),
                "to", flight.getTo(),
                "carrier", flight.getCarrier(),
                "departureTime", flight.getDepartureTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                "arrivalTime", flight.getArrivalTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );
    }
}