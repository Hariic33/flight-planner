package io.codelex.flightplanner.domain;

import jakarta.validation.constraints.NotBlank;

public class Airport {
    private String country;
    private String city;
    private String airport;

    public Airport(String country, String city, String airport) {
        this.country = country;
        this.city = city;
        this.airport = airport;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(@NotBlank String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(@NotBlank String city) {
        this.city = city;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(@NotBlank String airport) {
        this.airport = airport;
    }

}