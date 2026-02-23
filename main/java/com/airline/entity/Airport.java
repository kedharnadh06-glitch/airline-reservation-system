package com.airline.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "airports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Airport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "airport_id")
    private Integer airportId;
    
    @Column(name = "airport_code", unique = true, nullable = false, length = 10)
    private String airportCode;
    
    @Column(name = "airport_name", nullable = false, length = 100)
    private String airportName;
    
    @Column(nullable = false, length = 50)
    private String city;
    
    @Column(nullable = false, length = 50)
    private String country;
    
    @Column(nullable = false, length = 50)
    private String timezone;
    
    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
}