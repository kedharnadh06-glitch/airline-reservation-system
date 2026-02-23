package com.airline.service;

import com.airline.entity.*;
import com.airline.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class BookingService {
    
    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final SeatInventoryRepository seatInventoryRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    
    private static final Random random = new Random();
    
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    
    public Booking getBookingById(Integer id) {
        return bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
    }
    
    public Booking getBookingByReference(String reference) {
        return bookingRepository.findByBookingReference(reference)
            .orElseThrow(() -> new RuntimeException("Booking not found: " + reference));
    }
    
    public List<Booking> getUserBookings(Integer userId) {
        return bookingRepository.findBookingsByUserId(userId);
    }
    
    @Transactional
    public Booking createBooking(Integer flightId, Integer userId, List<String> seatNumbers, 
                                  String passengerName, Payment.PaymentMethod paymentMethod) {
        // Validate flight
        Flight flight = flightRepository.findById(flightId)
            .orElseThrow(() -> new RuntimeException("Flight not found"));
        
        if (flight.getStatus() != Flight.FlightStatus.SCHEDULED) {
            throw new RuntimeException("Flight is not available for booking");
        }
        
        // Validate and lock seats
        for (String seatNumber : seatNumbers) {
            SeatInventory seat = seatInventoryRepository
                .findAndLockAvailableSeat(flightId, seatNumber)
                .orElseThrow(() -> new RuntimeException("Seat " + seatNumber + " is no longer available"));
        }
        
        // Calculate price
        BigDecimal totalAmount = calculateTotalPrice(flight, seatNumbers);
        
        // Get user
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Create booking
        String bookingReference = generateBookingReference();
        Booking booking = Booking.builder()
            .bookingReference(bookingReference)
            .user(user)
            .flight(flight)
            .bookingStatus(Booking.BookingStatus.PENDING)
            .totalAmount(totalAmount)
            .currency("USD")
            .bookingDate(LocalDateTime.now())
            .build();
        
        booking = bookingRepository.save(booking);
        
        // Create passenger
        Passenger passenger = Passenger.builder()
            .booking(booking)
            .firstName(passengerName)
            .lastName("Doe")
            .dateOfBirth("1990-01-01")
            .seatNumber(seatNumbers.get(0))
            .build();
        booking.getPassengers().add(passenger);
        
        // Create payment
        Payment payment = Payment.builder()
            .booking(booking)
            .paymentMethod(paymentMethod)
            .paymentStatus(Payment.PaymentStatus.PENDING)
            .amount(totalAmount)
            .currency("USD")
            .createdAt(LocalDateTime.now())
            .build();
        
        payment = paymentRepository.save(payment);
        booking.setPayment(payment);
        
        // Process payment (simulated)
        payment.setPaymentStatus(Payment.PaymentStatus.SUCCESS);
        payment.setTransactionId("TXN-" + System.currentTimeMillis());
        payment.setProcessedAt(LocalDateTime.now());
        paymentRepository.save(payment);
        
        // Confirm booking
        booking.setBookingStatus(Booking.BookingStatus.CONFIRMED);
        
        // Mark seats as unavailable
        for (String seatNumber : seatNumbers) {
            SeatInventory seat = seatInventoryRepository
                .findByFlightFlightIdAndSeatNumber(flightId, seatNumber)
                .orElse(null);
            if (seat != null) {
                seat.setIsAvailable(false);
                seatInventoryRepository.save(seat);
            }
        }
        
        return bookingRepository.save(booking);
    }
    
    private BigDecimal calculateTotalPrice(Flight flight, List<String> seatNumbers) {
        BigDecimal total = BigDecimal.ZERO;
        
        for (String seatNumber : seatNumbers) {
            if (seatNumber.startsWith("F")) {
                total = total.add(flight.getBaseFirstClassPrice());
            } else if (seatNumber.startsWith("B")) {
                total = total.add(flight.getBaseBusinessPrice());
            } else {
                total = total.add(flight.getBaseEconomyPrice());
            }
        }
        
        return total;
    }
    
    private String generateBookingReference() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
    
    @Transactional
    public Booking cancelBooking(Integer bookingId) {
        Booking booking = getBookingById(bookingId);
        
        if (booking.getBookingStatus() != Booking.BookingStatus.CONFIRMED) {
            throw new RuntimeException("Only confirmed bookings can be cancelled");
        }
        
        booking.setBookingStatus(Booking.BookingStatus.CANCELLED);
        booking.setCancellationDate(LocalDateTime.now());
        
        return bookingRepository.save(booking);
    }
}