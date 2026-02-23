package com.airline.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seat_inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatInventory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Integer seatId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;
    
    @Column(name = "seat_number", nullable = false, length = 5)
    private String seatNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "seat_class", nullable = false)
    private SeatClass seatClass;
    
    @Column(name = "is_available")
    @Builder.Default
    private Boolean isAvailable = true;
    
    @Column(name = "seat_position", length = 20)
    private String seatPosition;
    
    public enum SeatClass {
        ECONOMY, BUSINESS, FIRST_CLASS
    }
}