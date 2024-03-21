package com.hrs.booking.jpa;

import com.hrs.booking.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    int countByName(String name);

    Privilege getOneByName(String privilege);
}
