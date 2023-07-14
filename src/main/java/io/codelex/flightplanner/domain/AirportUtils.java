package io.codelex.flightplanner.domain;

public class AirportUtils {
    private AirportUtils() {
    }

    public static boolean isEmpty(Airport airport) {
        return airport == null || airport.getCountry() == null || airport.getCity() == null || airport.getAirport() == null ||
                airport.getCountry().isEmpty() || airport.getCity().isEmpty() || airport.getAirport().isEmpty();
    }

    public static boolean equalsIgnoreCase(Airport airport1, Airport airport2) {
        if (airport1 == null || airport2 == null) {
            return false;
        }
        return airport1.getCountry().equalsIgnoreCase(airport2.getCountry())
                && airport1.getCity().equalsIgnoreCase(airport2.getCity())
                && airport1.getAirport().equalsIgnoreCase(airport2.getAirport());
    }
}