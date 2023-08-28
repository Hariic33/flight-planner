package io.codelex.flightplanner.dto;

import java.time.LocalDate;

public record FlightDTO(String from, String to, LocalDate departureDate) {

}