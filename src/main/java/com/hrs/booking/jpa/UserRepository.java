package com.hrs.booking.jpa;

import com.hrs.booking.entity.HRSUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// JPA Repository Interface for Users
@Repository
public interface UserRepository extends JpaRepository<HRSUser, Long> {

    //    JPA Method to find a User by Username
    HRSUser findByUsername(String superAdmin);

    //    JPA Method to find a User by email or username.
    HRSUser findByEmailOrUsername(String username, String username1);

    //    JPA Method to find a User by Email.
    HRSUser findByEmail(String email);

    //    JPA Method to find a User by UUID.
    Optional<HRSUser> findByUuid(String id);
}
