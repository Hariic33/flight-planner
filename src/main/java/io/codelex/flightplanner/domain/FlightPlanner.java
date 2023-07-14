package io.codelex.flightplanner.domain;

import java.util.Objects;

public class FlightPlanner {
    private Airport from;
    private Airport to;
    private String carrier;
    private String departureTime;
    private String arrivalTime;


    public FlightPlanner(Airport from, Airport to, String carrier, String departureTime, String arrivalTime) {
        this.from = from;
        this.to = to;
        this.carrier = carrier;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
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

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightPlanner flightPlanner = (FlightPlanner) o;
        return Objects.equals(from, flightPlanner.from)
                && Objects.equals(to, flightPlanner.to)
                && Objects.equals(carrier, flightPlanner.carrier)
                && Objects.equals(departureTime, flightPlanner.departureTime)
                && Objects.equals(arrivalTime, flightPlanner.arrivalTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(to, carrier, departureTime, arrivalTime);
    }
}