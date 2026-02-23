package com.airline.repository;

import com.airline.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {
    
    Optional<Flight> findByFlightNumber(String flightNumber);
    
    @Query("SELECT f FROM Flight f WHERE f.departureAirport.airportCode = :departureCode " +
           "AND f.arrivalAirport.airportCode = :arrivalCode " +
           "AND DATE(f.departureTime) = :date " +
           "AND f.status = 'SCHEDULED'")
    List<Flight> searchFlights(@Param("departureCode") String departureCode,
                                @Param("arrivalCode") String arrivalCode,
                                @Param("date") LocalDateTime date);
    
    List<Flight> findByStatus(Flight.FlightStatus status);
}