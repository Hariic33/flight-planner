package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.domain.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("SELECT f FROM Flight f WHERE f.from.airport = :fromAirport AND f.to.airport = :toAirport AND DATE(f.departureTime) = :departureDate")
    List<Flight> searchForFlight(@Param("fromAirport") String from, @Param("toAirport") String to, @Param("departureDate") LocalDate departure);

}