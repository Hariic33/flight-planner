package io.codelex.flightplanner.configuration;

import io.codelex.flightplanner.repository.FlightInMemoryRepository;
import io.codelex.flightplanner.repository.FlightRepository;
import io.codelex.flightplanner.service.FlightDatabaseService;
import io.codelex.flightplanner.service.FlightInMemoryService;
import io.codelex.flightplanner.service.FlightService;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@Configuration
public class FlightConfiguration {

    @Bean
    @ConditionalOnProperty(prefix="flight", name="service.version", havingValue = "in-memory")
    public FlightService getInMemoryVersion(FlightInMemoryRepository flightInMemoryRepository) {
        return new FlightInMemoryService(flightInMemoryRepository);
    }

    @Bean
    @ConditionalOnProperty(prefix="flight", name="service.version", havingValue = "database")
    public FlightService getDatabaseVersion(FlightRepository flightRepository) {
        return new FlightDatabaseService(flightRepository);
    }
}