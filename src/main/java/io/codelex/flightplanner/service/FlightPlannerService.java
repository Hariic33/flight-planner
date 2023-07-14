package io.codelex.flightplanner.service;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.AirportUtils;
import io.codelex.flightplanner.domain.FlightPlanner;
import io.codelex.flightplanner.dto.FlightPlannerDTO;
import io.codelex.flightplanner.exception.*;
import io.codelex.flightplanner.repository.FlightPlannerRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@Service
public class FlightPlannerService {
    private final FlightPlannerRepository flightPlannerRepository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public FlightPlannerService(FlightPlannerRepository flightPlannerRepository) {
        this.flightPlannerRepository = flightPlannerRepository;
    }

    public void clearFlights() {
        flightPlannerRepository.clearFlightList();
    }

    public void addFlights(@Valid FlightPlannerDTO flight) {
        if (flightPlannerRepository.flightAlreadyExists(flight)) {
            throw new FlightAlreadyExistsException();
        }

        validateAirports(flight.getFrom(), flight.getTo());
        flightPlannerRepository.addFlights(flight);
    }

    public boolean validateFlightRequest(FlightPlanner flight) {
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
        FlightPlannerDTO flight = getFlightById(id);
        if (flight == null) {
            throw new FlightNotFoundException();
        }

        flightPlannerRepository.deleteFlight(flight);
    }

    public FlightPlannerDTO getFlightById(String id) {
        FlightPlannerDTO flight = flightPlannerRepository.getFlightById(id);
        if (flight == null) {
            throw new FlightNotFoundException();
        }

        return flight;
    }

    public List<Airport> searchAirports(String search) {
        return flightPlannerRepository.getFilteredMatchList(search.toLowerCase().trim());
    }

    public List<FlightPlannerDTO> searchFlights(String from, String to, String departureDate) {
        if (from == null || to == null || departureDate == null) {
            throw new InvalidSearchException();
        }

        if (from.equalsIgnoreCase(to)) {
            throw new SameAirportsException();
        }

        List<FlightPlannerDTO> flights = flightPlannerRepository.searchFlights(from, to, departureDate);

        if (flights.isEmpty()) {
            return List.of();
        }

        return flights;
    }

    public FlightPlannerDTO mapToDTO(FlightPlanner flight) {
        LocalDateTime departure = parseDateTime(flight.getDepartureTime());
        LocalDateTime arrival = parseDateTime(flight.getArrivalTime());

        if (departure.isAfter(arrival) || departure.isEqual(arrival)) {
            throw new InvalidFlightDateException();
        }

        return new FlightPlannerDTO(
                UUID.randomUUID().toString(),
                flight.getFrom(),
                flight.getTo(),
                flight.getCarrier(),
                departure,
                arrival
        );
    }

    private LocalDateTime parseDateTime(String dateTimeString) {
        try {
            return LocalDateTime.parse(dateTimeString, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InvalidFlightDateException();
        }
    }
}