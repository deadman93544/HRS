package com.hrs.booking.view.booking;

import com.hrs.booking.enums.BookingStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class BookingRequest {
    private String uid;
    private String roomId;
    private Date checkIn;
    private Date checkOut;
    private Double price;
    private BookingStatus bookingStatus;
    private Boolean paid;
}
