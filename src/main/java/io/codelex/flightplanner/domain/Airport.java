package io.codelex.flightplanner.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Component;

@Entity
@Component
@Table(name = "airport")
@JsonIgnoreProperties(value = {"id"})
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "airport")
    private String airport;

    @NotBlank
    @Column(name = "city")
    private String city;

    @NotBlank
    @Column(name = "country")
    private String country;

    public Airport() {
    }

    public Airport(String airport, String city, String country) {
        this.airport = airport;
        this.city = city;
        this.country = country;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}