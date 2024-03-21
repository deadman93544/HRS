package com.hrs.booking.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

//The Privilege class represents a privilege entity within a booking application,
// typically used for defining specific actions or access levels that users with certain roles are allowed to perform.
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Privilege extends BaseEntity
{

    @Column(nullable = false, length = 50)
    private String name;

    public Privilege(Privilege p)
    {
        this.name = p.name;
    }

}
