package com.airline.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "aircrafts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aircraft {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aircraft_id")
    private Integer aircraftId;
    
    @Column(name = "aircraft_code", unique = true, nullable = false, length = 10)
    private String aircraftCode;
    
    @Column(name = "aircraft_name", nullable = false, length = 100)
    private String aircraftName;
    
    @Column(length = 50)
    private String manufacturer;
    
    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;
    
    @Column(name = "economy_seats", nullable = false)
    private Integer economySeats;
    
    @Column(name = "business_seats", nullable = false)
    private Integer businessSeats;
    
    @Column(name = "first_class_seats")
    @Builder.Default
    private Integer firstClassSeats = 0;
    
    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
}