package com.hrs.booking.entity;

import com.hrs.booking.view.room.RoomRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

//The Room class represents a room entity within a hotel booking system. This class includes properties to describe the room and its characteristics
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Room extends BaseEntity{
    private String name;

    @ManyToOne
    private Hotel hotel;

    private BigDecimal price;

    private Long timeFrameInHours;

    private String roomType;

    private Long maxCapacity;

    private String roomView;

    private Long floor;

    private Boolean smokingAllowed;

    public void setValues(RoomRequest request){
        this.name = request.getName();
        this.price = BigDecimal.valueOf(request.getPrice());
        this.timeFrameInHours = request.getTimeframe();
        this.roomType = request.getRoomType().toString();
        this.maxCapacity = request.getMaxCapacity();
        this.roomView = request.getRoomView().toString();
        this.floor = request.getFloor();
        this.smokingAllowed = request.getSmokingAllowed();
    }

}
