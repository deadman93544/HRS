package com.hrs.booking.jpa;

import com.hrs.booking.entity.Hotel;
import com.hrs.booking.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByUuid(String uid);

    Page<Room> findAllByHotelIn(List<Hotel> hotels, Pageable paging);
}
