package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.dto.FlightPlannerDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class FlightPlannerRepository {
    private final List<FlightPlannerDTO> flightList;

    public FlightPlannerRepository() {
        this.flightList = new CopyOnWriteArrayList<>();
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

    public List<FlightPlannerDTO> getFlights() {
        return flightList;
    }
}