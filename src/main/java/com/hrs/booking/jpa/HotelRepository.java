package com.hrs.booking.jpa;

import com.hrs.booking.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// JPA Repository Interface for Hotels
@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    //    JPA Method to fetch a Page of Hotel By Name Search
    Page<Hotel> findAllByNameContaining(String searchKey, Pageable paging);

    //    JPA Method to fetch a List of Hotel By Name Search
    List<Hotel> findAllByNameContaining(String searchKey);

    //    JPA Method to find a Hotel By UUID.
    Optional<Hotel> findByUuid(String id);
}
