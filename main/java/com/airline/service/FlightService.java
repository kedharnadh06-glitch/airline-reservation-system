package com.airline.service;

import com.airline.entity.Flight;
import com.airline.entity.SeatInventory;
import com.airline.repository.FlightRepository;
import com.airline.repository.SeatInventoryRepository;
import com.airline.repository.AirportRepository;
import com.airline.repository.AircraftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {
    
    private final FlightRepository flightRepository;
    private final SeatInventoryRepository seatInventoryRepository;
    private final AirportRepository airportRepository;
    private final AircraftRepository aircraftRepository;
    
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }
    
    public Flight getFlightById(Integer id) {
        return flightRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Flight not found with id: " + id));
    }
    
    public Flight getFlightByNumber(String flightNumber) {
        return flightRepository.findByFlightNumber(flightNumber)
            .orElseThrow(() -> new RuntimeException("Flight not found: " + flightNumber));
    }
    
    public List<Flight> searchFlights(String departureCode, String arrivalCode, LocalDateTime date) {
        return flightRepository.searchFlights(departureCode, arrivalCode, date);
    }
    
    public List<Flight> getFlightsByStatus(Flight.FlightStatus status) {
        return flightRepository.findByStatus(status);
    }
    
    @Transactional
    public Flight createFlight(Flight flight) {
        Flight savedFlight = flightRepository.save(flight);
        initializeSeatInventory(savedFlight);
        return savedFlight;
    }
    
    private void initializeSeatInventory(Flight flight) {
        int economySeats = flight.getAircraft().getEconomySeats();
        int businessSeats = flight.getAircraft().getBusinessSeats();
        int firstClassSeats = flight.getAircraft().getFirstClassSeats();
        
        // Economy seats
        for (int i = 1; i <= economySeats; i++) {
            String seatNumber = "E" + String.format("%03d", i);
            String position = (i % 6 == 1 || i % 6 == 0) ? "WINDOW" : 
                             (i % 6 == 2 || i % 6 == 5) ? "MIDDLE" : "AISLE";
            SeatInventory seat = SeatInventory.builder()
                .flight(flight)
                .seatNumber(seatNumber)
                .seatClass(SeatInventory.SeatClass.ECONOMY)
                .isAvailable(true)
                .seatPosition(position)
                .build();
            seatInventoryRepository.save(seat);
        }
        
        // Business seats
        for (int i = 1; i <= businessSeats; i++) {
            String seatNumber = "B" + String.format("%02d", i);
            String position = (i % 4 == 1 || i % 4 == 0) ? "WINDOW" : "AISLE";
            SeatInventory seat = SeatInventory.builder()
                .flight(flight)
                .seatNumber(seatNumber)
                .seatClass(SeatInventory.SeatClass.BUSINESS)
                .isAvailable(true)
                .seatPosition(position)
                .build();
            seatInventoryRepository.save(seat);
        }
        
        // First class seats
        for (int i = 1; i <= firstClassSeats; i++) {
            String seatNumber = "F" + String.format("%02d", i);
            String position = (i % 2 == 1) ? "WINDOW" : "AISLE";
            SeatInventory seat = SeatInventory.builder()
                .flight(flight)
                .seatNumber(seatNumber)
                .seatClass(SeatInventory.SeatClass.FIRST_CLASS)
                .isAvailable(true)
                .seatPosition(position)
                .build();
            seatInventoryRepository.save(seat);
        }
    }
    
    @Transactional
    public Flight updateFlightStatus(Integer flightId, Flight.FlightStatus status) {
        Flight flight = getFlightById(flightId);
        flight.setStatus(status);
        return flightRepository.save(flight);
    }
    
    public List<SeatInventory> getAvailableSeatsForFlight(Integer flightId) {
        return seatInventoryRepository.findByFlightFlightIdAndIsAvailableTrue(flightId);
    }
    
    public int getAvailableSeatsCount(Integer flightId) {
        return getAvailableSeatsForFlight(flightId).size();
    }
}