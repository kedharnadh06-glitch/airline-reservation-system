package com.airline.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "flights")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flight {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_id")
    private Integer flightId;
    
    @Column(name = "flight_number", unique = true, nullable = false, length = 20)
    private String flightNumber;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "departure_airport_id", nullable = false)
    private Airport departureAirport;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "arrival_airport_id", nullable = false)
    private Airport arrivalAirport;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aircraft_id", nullable = false)
    private Aircraft aircraft;
    
    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;
    
    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;
    
    @Column(name = "flight_duration_minutes", nullable = false)
    private Integer flightDurationMinutes;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private FlightStatus status = FlightStatus.SCHEDULED;
    
    @Column(name = "base_economy_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseEconomyPrice;
    
    @Column(name = "base_business_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseBusinessPrice;
    
    @Column(name = "base_first_class_price", precision = 10, scale = 2)
    private BigDecimal baseFirstClassPrice;
    
    public enum FlightStatus {
        SCHEDULED, DELAYED, CANCELLED, COMPLETED, BOARDING
    }
}