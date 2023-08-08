package io.codelex.flightplanner.service;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.dto.FlightDTO;
import io.codelex.flightplanner.exception.FlightAlreadyExistsException;
import io.codelex.flightplanner.exception.FlightException;
import io.codelex.flightplanner.repository.AirportRepository;
import io.codelex.flightplanner.repository.FlightRepository;
import io.codelex.flightplanner.exception.FlightNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class FlightDatabaseService implements FlightService {
    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;

    public FlightDatabaseService(FlightRepository flightRepository, AirportRepository airportRepository) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
    }

    @Override
    public void clearFlights() {
        flightRepository.deleteAll();
    }

    @Override
    public synchronized Flight addFlight(Flight flight) {
        validateFlight(flight);
        return flightRepository.save(flight);
    }

    void validateFlight(Flight flight) {
        Airport from = flight.getFrom();
        Airport to = flight.getTo();
        LocalDateTime departureTime = flight.getDepartureTime();
        LocalDateTime arrivalTime = flight.getArrivalTime();
        String carrier = flight.getCarrier();

        if (flightRepository.findAll().contains(flight)) {
            throw new FlightAlreadyExistsException();
        }

        if (flight.getFrom().getAirport().trim().equalsIgnoreCase(flight.getTo().getAirport().trim())) {
            throw new FlightException("Departure and arrival airports cannot be the same");
        }

        if (Stream.of(from, to, departureTime, arrivalTime)
                .anyMatch(Objects::isNull) || !departureTime.isBefore(arrivalTime) ||
                Stream.of(carrier, from.getCountry(), from.getCity(), from.getAirport(), to.getCountry(), to.getCity(), to.getAirport())
                        .anyMatch(value -> value == null || value.trim().isEmpty())) {
            throw new FlightException("Invalid flight request");
        }
    }

    @Override
    public List<Airport> searchAirports(String search) {
        return airportRepository.searchAirports(search.toLowerCase().trim());
    }

    @Override
    public boolean deleteFlight(Long id) {
        if (flightRepository.existsById(id)) {
            flightRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Flight getFlightById(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(FlightNotFoundException::new);
    }

    @Override
    public Map<String, Object> searchFlights(FlightDTO dto) {
        String airportFrom = dto.from();
        String airportTo = dto.to();

        if (airportFrom == null || airportTo == null) {
            throw new FlightException("Airport from and airport to cannot be null");
        }

        if (airportFrom.equals(airportTo)) {
            throw new FlightException("Departure and arrival airports cannot be the same");
        }

        List<Flight> flights = flightRepository.searchForFlight(
                dto.from(), dto.to(), dto.departureDate()
        );

        return Map.of("items", flights, "page", 0, "totalItems", flights.size());
    }

    @Override
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