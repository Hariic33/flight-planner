package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.dto.FlightPlannerDTO;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FlightPlannerRepository {
    private final List<FlightPlannerDTO> flightList;

    public FlightPlannerRepository(List<FlightPlannerDTO> flightList) {
        this.flightList = flightList;
    }

    public void clearFlightList() {
        flightList.clear();
    }

    public void addFlights(FlightPlannerDTO flight) {
        synchronized (flightList) {
            if (flightList.contains(flight)) {
                return;
            }
            flightList.add(flight);
        }
    }

    public boolean flightAlreadyExists(FlightPlannerDTO flight) {
        synchronized (flightList) {
            return flightList.stream()
                    .anyMatch(f -> f.equals(flight));
        }
    }

    public void deleteFlight(FlightPlannerDTO flight) {
        synchronized (flightList) {
            flightList.remove(flight);
        }
    }

    public FlightPlannerDTO getFlightById(String id) {
        synchronized (flightList) {
            return flightList.stream()
                    .filter(f -> f.getId().equals(id))
                    .findFirst()
                    .orElse(null);
        }
    }

    public List<Airport> getFilteredMatchList(String match) {
        return flightList.stream()
                .map(FlightPlannerDTO::getFrom)
                .filter(from -> from.getAirport().toLowerCase().contains(match)
                        || from.getCity().toLowerCase().contains(match)
                        || from.getCountry().toLowerCase().contains(match))
                .map(from -> new Airport(from.getCountry(), from.getCity(), from.getAirport()))
                .toList();
    }

    public List<FlightPlannerDTO> searchFlights(String from, String to, String departureDate) {
        return flightList.stream()
                .filter(flight -> flight.getFrom().getAirport().equalsIgnoreCase(from)
                        && flight.getTo().getAirport().equalsIgnoreCase(to)
                        && flight.getDepartureTime().toLocalDate().isEqual(LocalDate.parse(departureDate)))
                .collect(Collectors.toList());
    }

    public List<FlightPlannerDTO> getFlights() {
        return flightList;
    }
}