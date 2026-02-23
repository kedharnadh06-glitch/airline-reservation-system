package com.airline;

import com.airline.entity.*;
import com.airline.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final AirportRepository airportRepository;
    private final AircraftRepository aircraftRepository;
    private final FlightRepository flightRepository;
    
    @Override
    public void run(String... args) {
        if (airportRepository.count() == 0) {
            initData();
        }
    }
    
    private void initData() {
        // Create Airports
        Airport jfk = airportRepository.save(Airport.builder()
            .airportCode("JFK")
            .airportName("John F. Kennedy")
            .city("New York")
            .country("USA")
            .timezone("America/New_York")
            .build());
        
        Airport lax = airportRepository.save(Airport.builder()
            .airportCode("LAX")
            .airportName("Los Angeles")
            .city("Los Angeles")
            .country("USA")
            .timezone("America/Los_Angeles")
            .build());
        
        // Create Aircraft
        Aircraft aircraft = aircraftRepository.save(Aircraft.builder()
            .aircraftCode("B737")
            .aircraftName("Boeing 737")
            .manufacturer("Boeing")
            .totalSeats(180)
            .economySeats(150)
            .businessSeats(24)
            .firstClassSeats(6)
            .build());
        
        // Create Flights
        flightRepository.save(Flight.builder()
            .flightNumber("AA100")
            .departureAirport(jfk)
            .arrivalAirport(lax)
            .aircraft(aircraft)
            .departureTime(LocalDateTime.now().plusDays(7).withHour(8).withMinute(0))
            .arrivalTime(LocalDateTime.now().plusDays(7).withHour(11).withMinute(30))
            .flightDurationMinutes(330)
            .status(Flight.FlightStatus.SCHEDULED)
            .baseEconomyPrice(new BigDecimal("299.00"))
            .baseBusinessPrice(new BigDecimal("599.00"))
            .baseFirstClassPrice(new BigDecimal("1299.00"))
            .build());
        
        System.out.println(">>> Sample data initialized!");
    }
}