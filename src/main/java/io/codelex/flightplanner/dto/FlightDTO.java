package io.codelex.flightplanner.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class FlightDTO {

        @NotBlank
        private String from;

        @NotBlank
        private String to;

        @JsonFormat(pattern = "yyyy-MM-dd")
        @NotNull
        private LocalDate departureDate;

        public FlightDTO(String from, String to, LocalDate departureDate) {
                this.from = from;
                this.to = to;
                this.departureDate = departureDate;
        }

        public String getFrom() {
                return from;
        }

        public void setFrom(String from) {
                this.from = from;
        }

        public String getTo() {
                return to;
        }

        public void setTo(String to) {
                this.to = to;
        }

        public LocalDate getDepartureDate() {
                return departureDate;
        }

        public void setDepartureDate(LocalDate departureDate) {
                this.departureDate = departureDate;
        }
}