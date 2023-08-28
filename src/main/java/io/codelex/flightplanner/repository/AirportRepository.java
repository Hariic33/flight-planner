package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.domain.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AirportRepository extends JpaRepository<Airport, Long> {

    @Query("SELECT DISTINCT f.from FROM Flight f WHERE LOWER(f.from.airport) LIKE %:search%" +
            " OR LOWER(f.from.city) LIKE %:search%" +
            " OR LOWER(f.from.country) LIKE %:search%")
    List<Airport> searchAirports(String search);

}
