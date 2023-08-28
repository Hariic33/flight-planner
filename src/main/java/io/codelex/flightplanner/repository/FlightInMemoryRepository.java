package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.domain.Flight;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class FlightInMemoryRepository {
    private final Map<Long, Flight> flightMap = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public void clearFlightList() {
        flightMap.clear();
        nextId.set(1);
    }

    public void addFlight(Flight flight) {
        flight.setId(nextId.getAndIncrement());
        flightMap.put(flight.getId(), flight);
    }

    public void deleteFlight(Flight flight) {
        flightMap.remove(flight.getId());
    }

    public Flight getFlightById(Long id) {
        return flightMap.get(id);
    }

    public List<Flight> getFlights() {
        return new ArrayList<>(flightMap.values());
    }
}