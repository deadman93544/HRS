package com.hrs.booking.view.hotel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HotelRequest {
    private String uid;
    private String name;
    private String contactName;
    private String contactNumber;
    private String address;
    private Double lat;
    private Double lng;
    private String description;
    private Double rating;

}
