package com.hrs.booking.jpa;

import com.hrs.booking.entity.HRSUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<HRSUser, Long> {
    HRSUser findByUsername(String superAdmin);

    HRSUser findByEmailOrUsername(String username, String username1);

    HRSUser findByEmail(String email);

    HRSUser findOneByUuid(String id);

    Optional<HRSUser> findByUuid(String id);
}
