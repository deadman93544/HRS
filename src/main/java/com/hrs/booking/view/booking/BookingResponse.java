package com.hrs.booking.view.booking;

import com.hrs.booking.entity.Booking;
import com.hrs.booking.enums.BookingStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class BookingResponse {
    private String uid;
    private String roomId;
    private String roomName;
    private Date checkIn;
    private Date checkOut;
    private Double price;
    private BookingStatus bookingStatus;
    private Boolean paid;

    public BookingResponse(Booking booking){
        this.uid = booking.getUuid();
        this.roomId = booking.getRoom().getUuid();
        this.roomName = booking.getRoom().getName();
        this.checkIn = new Date(booking.getCheckIn() * 3600 * 1000);
        this.checkOut = new Date(booking.getCheckOut() * 3600 * 1000);
        this.price = booking.getPrice().doubleValue();
        this.bookingStatus = BookingStatus.valueOf(booking.getBookingStatus());
        this.paid = booking.isPaid();
    }
}
