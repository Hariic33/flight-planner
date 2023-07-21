package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.dto.FlightDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class FlightRepository {
    private final List<FlightDTO> flightList;

    public FlightRepository() {
        this.flightList = new CopyOnWriteArrayList<>();
    }

    public void clearFlightList() {
        flightList.clear();
    }

    public void addFlights(FlightDTO flight) {
        synchronized (flightList) {
            if (flightList.contains(flight)) {
                return;
            }
            flightList.add(flight);
        }
    }

    public void deleteFlight(FlightDTO flight) {
        synchronized (flightList) {
            flightList.remove(flight);
        }
    }

    public FlightDTO getFlightById(String id) {
        synchronized (flightList) {
            return flightList.stream()
                    .filter(f -> f.getId().equals(id))
                    .findFirst()
                    .orElse(null);
        }
    }

    public List<FlightDTO> getFlights() {
        return flightList;
    }
}