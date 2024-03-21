package com.hrs.booking.jpa;

import com.hrs.booking.entity.Booking;
import com.hrs.booking.entity.HRSUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.room.id = :roomId AND NOT (b.checkOut <= :checkIn OR b.checkIn >= :checkOut)")
    long countOverlappingBookings(@Param("roomId") Long roomId, @Param("checkIn") Long checkIn, @Param("checkOut") Long checkOut);

    Page<Booking> findAllByCreatedBy(HRSUser user, Pageable paging);

    Optional<Booking> findByUuid(String id);
}
