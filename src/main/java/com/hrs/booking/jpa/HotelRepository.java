package com.hrs.booking.jpa;

import com.hrs.booking.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Page<Hotel> findAllByNameContaining(String searchKey, Pageable paging);

    List<Hotel> findAllByNameContaining(String searchKey);

    Optional<Hotel> findByUuid(String id);
}
