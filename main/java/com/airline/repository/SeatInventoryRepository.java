package com.airline.repository;

import com.airline.entity.SeatInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeatInventoryRepository extends JpaRepository<SeatInventory, Integer> {
    
    List<SeatInventory> findByFlightFlightIdAndIsAvailableTrue(Integer flightId);
    
    List<SeatInventory> findByFlightFlightIdAndSeatClass(Integer flightId, SeatInventory.SeatClass seatClass);
    
    Optional<SeatInventory> findByFlightFlightIdAndSeatNumber(Integer flightId, String seatNumber);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM SeatInventory s WHERE s.flight.flightId = :flightId " +
           "AND s.seatNumber = :seatNumber AND s.isAvailable = true")
    Optional<SeatInventory> findAndLockAvailableSeat(@Param("flightId") Integer flightId,
                                                      @Param("seatNumber") String seatNumber);
}