package io.codelex.flightplanner.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic(withDefaults())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("admin-api/flights").authenticated()
                        .requestMatchers("/admin-api/flights/{flightId}").authenticated()
                        .anyRequest().permitAll()
                );
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
