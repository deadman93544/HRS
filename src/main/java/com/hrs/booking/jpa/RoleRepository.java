package com.hrs.booking.jpa;

import com.hrs.booking.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getOneByName(String techadmin);

    Role findByName(String name);

    Role findOneByUuid(String id);
}
