package io.codelex.flightplanner.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.codelex.flightplanner.domain.Airport;

import java.time.LocalDateTime;
import java.util.Objects;

public class FlightPlannerDTO {
    private String id;
    private Airport from;
    private Airport to;
    private String carrier;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departureTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime arrivalTime;

    public FlightPlannerDTO(String id, Airport from, Airport to, String carrier, LocalDateTime departureTIme, LocalDateTime arrivalTime) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.carrier = carrier;
        this.departureTime = departureTIme;
        this.arrivalTime = arrivalTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Airport getFrom() {
        return from;
    }

    public void setFrom(Airport from) {
        this.from = from;
    }

    public Airport getTo() {
        return to;
    }

    public void setTo(Airport to) {
        this.to = to;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightPlannerDTO flightDTO = (FlightPlannerDTO) o;
        return Objects.equals(from.getAirport(), flightDTO.from.getAirport()) &&
                Objects.equals(to.getAirport(), flightDTO.to.getAirport()) &&
                Objects.equals(carrier, flightDTO.carrier) &&
                Objects.equals(departureTime, flightDTO.departureTime) &&
                Objects.equals(arrivalTime, flightDTO.arrivalTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, carrier, departureTime, arrivalTime);
    }
}