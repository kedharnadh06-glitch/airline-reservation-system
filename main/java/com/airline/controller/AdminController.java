package com.airline.controller;

import com.airline.entity.Flight;
import com.airline.service.FlightService;
import com.airline.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final FlightService flightService;
    private final ReportService reportService;
    
    @PostMapping("/flights")
    public ResponseEntity<Flight> createFlight(@RequestBody Map<String, Object> request) {
        Flight flight = Flight.builder()
            .flightNumber((String) request.get("flightNumber"))
            .build();
        return ResponseEntity.ok(flightService.createFlight(flight));
    }
    
    @PutMapping("/flights/{id}/status")
    public ResponseEntity<Flight> updateStatus(
            @PathVariable Integer id,
            @RequestParam String status) {
        Flight.FlightStatus flightStatus = Flight.FlightStatus.valueOf(status);
        return ResponseEntity.ok(flightService.updateFlightStatus(id, flightStatus));
    }
    
    @GetMapping("/reports/sales")
    public ResponseEntity<Map<String, Object>> getSalesReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return ResponseEntity.ok(reportService.generateSalesReport(start, end));
    }
    
    @GetMapping("/reports/occupancy/{flightId}")
    public ResponseEntity<Map<String, Object>> getOccupancyReport(@PathVariable Integer flightId) {
        return ResponseEntity.ok(reportService.generateOccupancyReport(flightId));
    }
}