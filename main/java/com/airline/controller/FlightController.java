package com.airline.controller;

import com.airline.entity.Flight;
import com.airline.entity.SeatInventory;
import com.airline.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
public class FlightController {
    
    private final FlightService flightService;
    
    @GetMapping
    public ResponseEntity<List<Flight>> getAllFlights() {
        return ResponseEntity.ok(flightService.getAllFlights());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable Integer id) {
        return ResponseEntity.ok(flightService.getFlightById(id));
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Flight>> searchFlights(
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam String date) {
        LocalDateTime dateTime = LocalDateTime.parse(date);
        return ResponseEntity.ok(flightService.searchFlights(departure, arrival, dateTime));
    }
    
    @GetMapping("/{flightId}/seats")
    public ResponseEntity<List<SeatInventory>> getAvailableSeats(@PathVariable Integer flightId) {
        return ResponseEntity.ok(flightService.getAvailableSeatsForFlight(flightId));
    }
    
    @GetMapping("/{flightId}/availability")
    public ResponseEntity<Object> getAvailability(@PathVariable Integer flightId) {
        int available = flightService.getAvailableSeatsCount(flightId);
        return ResponseEntity.ok(java.util.Map.of(
            "flightId", flightId,
            "availableSeats", available
        ));
    }
}