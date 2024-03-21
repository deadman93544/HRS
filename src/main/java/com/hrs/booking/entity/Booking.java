package com.hrs.booking.entity;

import com.hrs.booking.view.booking.BookingRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Booking extends BaseEntity{

    @ManyToOne
    private Room room;

    private Long checkIn;

    private Long checkOut;

    private BigDecimal price;

    private String bookingStatus;

    private boolean paid = true;

    public void setValues(BookingRequest request){
        this.checkIn = request.getCheckIn().getTime() / 1000 / 3600;
        this.checkOut = request.getCheckOut().getTime() / 1000 / 3600;
    }
}
