package com.airline.service;

import com.airline.entity.Booking;
import com.airline.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {
    
    private final BookingRepository bookingRepository;
    
    public Map<String, Object> generateSalesReport(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);
        
        List<Booking> bookings = bookingRepository.findAll().stream()
            .filter(b -> b.getBookingDate() != null && 
                        b.getBookingDate().isAfter(start) && 
                        b.getBookingDate().isBefore(end))
            .toList();
        
        Map<String, Object> report = new HashMap<>();
        report.put("period", startDate + " to " + endDate);
        report.put("totalBookings", bookings.size());
        report.put("totalRevenue", bookings.stream()
            .filter(b -> b.getBookingStatus() == Booking.BookingStatus.CONFIRMED)
            .map(Booking::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add));
        report.put("cancelledBookings", bookings.stream()
            .filter(b -> b.getBookingStatus() == Booking.BookingStatus.CANCELLED)
            .count());
        
        return report;
    }
    
    public Map<String, Object> generateOccupancyReport(Integer flightId) {
        List<Booking> bookings = bookingRepository.findAll().stream()
            .filter(b -> b.getFlight().getFlightId().equals(flightId))
            .filter(b -> b.getBookingStatus() == Booking.BookingStatus.CONFIRMED)
            .toList();
        
        int bookedSeats = bookings.stream()
            .mapToInt(b -> b.getPassengers().size())
            .sum();
        
        Map<String, Object> report = new HashMap<>();
        report.put("flightId", flightId);
        report.put("bookedSeats", bookedSeats);
        report.put("totalPassengers", bookings.size());
        
        return report;
    }
}