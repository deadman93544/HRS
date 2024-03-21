package com.hrs.booking.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role extends BaseEntity {


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_privilege",
            joinColumns = { @JoinColumn(name = "ROLE_ID") },
            inverseJoinColumns = { @JoinColumn(name = "PRIV_ID") })
    private Set<Privilege> privileges;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<HRSUser> users;

    @Column(nullable = false, columnDefinition = "bigint default 1")
    private Long authHours = 1L;

    public void addPrivilege(Privilege privilege)
    {
        if (privileges == null) privileges = new HashSet<>();
        privileges.add(privilege);
    }
}
