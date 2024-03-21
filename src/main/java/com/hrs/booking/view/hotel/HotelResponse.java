package com.hrs.booking.view.hotel;

import com.hrs.booking.entity.Hotel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HotelResponse {
    private String uid;
    private String name;
    private String contactName;
    private String contactNumber;
    private String address;
    private Double lat;
    private Double lng;
    private String description;
    private Double rating;

    public HotelResponse(Hotel hotel){
        this.uid = hotel.getUuid();
        this.name = hotel.getName();
        this.contactName = hotel.getName();
        this.contactNumber = hotel.getContactNumber();
        this.address = hotel.getAddress();
        this.lat = hotel.getLat().doubleValue();
        this.lng = hotel.getLng().doubleValue();
        this.description = hotel.getDescription();
        this.rating = hotel.getRating().doubleValue();
    }
}
