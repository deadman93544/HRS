package com.hrs.booking.entity;

import com.hrs.booking.view.hotel.HotelRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Hotel extends BaseEntity{

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 10)
    private String contactNumber;

    private String contactName;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(precision = 16, scale = 4)
    private BigDecimal lat;

    @Column(precision = 16, scale = 4)
    private BigDecimal lng;

    @Column(nullable = false, length = 200)
    private String description;

    private BigDecimal rating;

    public void setValues(HotelRequest request){
        this.name = request.getName();
        this.contactName = request.getContactName();
        this.contactNumber = request.getContactNumber();
        this.address = request.getAddress();
        this.lat = BigDecimal.valueOf(request.getLat());
        this.lng = BigDecimal.valueOf(request.getLng());
        this.description = request.getDescription();
        this.rating = BigDecimal.valueOf(request.getRating());
    }

}
