package com.airline.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "passengers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Passenger {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "passenger_id")
    private Integer passengerId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
    
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    
    @Column(name = "date_of_birth", nullable = false)
    private String dateOfBirth;
    
    @Column(name = "passport_number", length = 20)
    private String passportNumber;
    
    @Column(length = 50)
    private String nationality;
    
    @Column(name = "seat_number", length = 5)
    private String seatNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "meal_preference")
    @Builder.Default
    private MealPreference mealPreference = MealPreference.REGULAR;
    
    @Column(name = "special_requests", columnDefinition = "TEXT")
    private String specialRequests;
    
    public enum MealPreference {
        REGULAR, VEGETARIAN, VEGAN, HALAL, KOSHER
    }
}