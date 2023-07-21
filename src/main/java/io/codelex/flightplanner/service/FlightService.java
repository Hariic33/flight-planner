package io.codelex.flightplanner.service;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.AirportUtils;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.dto.FlightDTO;
import io.codelex.flightplanner.exception.*;
import io.codelex.flightplanner.repository.FlightRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FlightService {
    private final FlightRepository flightRepository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public void clearFlights() {
        flightRepository.clearFlightList();
    }

    public void addFlights(@Valid FlightDTO flight) {
        validateAirports(flight.getFrom(), flight.getTo());
        if (flightRepository.getFlights().contains(flight)) {
            throw new FlightAlreadyExistsException();
        }
        flightRepository.addFlights(flight);
    }

    public boolean validateFlightRequest(Flight flight) {
        if (flight == null) {
            return false;
        }

        Airport from = flight.getFrom();
        Airport to = flight.getTo();

        return from != null && to != null &&
                !AirportUtils.isEmpty(from) && !AirportUtils.isEmpty(to) &&
                isNotBlank(flight.getCarrier()) &&
                isNotBlank(from.getCountry()) && isNotBlank(from.getCity()) && isNotBlank(from.getAirport()) &&
                isNotBlank(to.getCountry()) && isNotBlank(to.getCity()) && isNotBlank(to.getAirport()) &&
                isNotBlank(flight.getDepartureTime()) && isNotBlank(flight.getArrivalTime());
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }

    public void validateAirports(Airport airportFrom, Airport airportTo) {
        if (AirportUtils.equalsIgnoreCase(airportFrom, airportTo) || airportFrom.getAirport().trim().equalsIgnoreCase(airportTo.getAirport().trim())) {
            throw new SameAirportsException();
        }
    }

    public void deleteFlight(String id) {
        FlightDTO flight = getFlightById(id);
        if (flight == null) {
            throw new FlightNotFoundException();
        }

        flightRepository.deleteFlight(flight);
    }

    public FlightDTO getFlightById(String id) {
        FlightDTO flight = flightRepository.getFlightById(id);
        if (flight == null) {
            throw new FlightNotFoundException();
        }

        return flight;
    }

    public List<Airport> searchAirports(String search) {
        String lowercaseSearch = search.toLowerCase().trim();
        return flightRepository.getFlights().stream()
                .map(FlightDTO::getFrom)
                .filter(from -> matchesAirport(from, lowercaseSearch))
                .map(from -> new Airport(from.getCountry(), from.getCity(), from.getAirport()))
                .toList();
    }

    private boolean matchesAirport(Airport airport, String search) {
        return airport.getAirport().toLowerCase().contains(search)
                || airport.getCity().toLowerCase().contains(search)
                || airport.getCountry().toLowerCase().contains(search);
    }

    public List<FlightDTO> searchFlights(String from, String to, String departureDate) {
        if (from == null || to == null || departureDate == null) {
            throw new InvalidSearchException();
        }

        if (from.equalsIgnoreCase(to)) {
            throw new SameAirportsException();
        }

        List<FlightDTO> flights = flightRepository.getFlights();
        return filterFlightsBySearchCriteria(flights, from, to, departureDate);
    }

    private List<FlightDTO> filterFlightsBySearchCriteria(List<FlightDTO> flights, String from, String to, String departureDate) {
        return flights.stream()
                .filter(flight -> matchesSearchCriteria(flight, from, to, departureDate))
                .collect(Collectors.toList());
    }

    private boolean matchesSearchCriteria(FlightDTO flight, String from, String to, String departureDate) {
        return flight.getFrom().getAirport().equalsIgnoreCase(from.toLowerCase())
                && flight.getTo().getAirport().equalsIgnoreCase(to.toLowerCase())
                && flight.getDepartureTime().toLocalDate().isEqual(LocalDate.parse(departureDate));
    }

    public FlightDTO mapToDTO(Flight flight) {
        LocalDateTime departure = LocalDateTime.parse(flight.getDepartureTime(), FORMATTER);
        LocalDateTime arrival = LocalDateTime.parse(flight.getArrivalTime(), FORMATTER);

        if (departure.isAfter(arrival) || departure.isEqual(arrival)) {
            throw new InvalidFlightDateException();
        }

        return new FlightDTO(flight.getFrom(), flight.getTo(), flight.getCarrier(), departure, arrival);
    }
}