package com.hrs.booking.view.room;

import com.hrs.booking.entity.Room;
import com.hrs.booking.enums.RoomType;
import com.hrs.booking.enums.RoomView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomResponse {
    private String uid;
    private String hotelId;
    private String hotelName;
    private String name;
    private Double price;
    private Long timeframe;
    private RoomType roomType;
    private Long maxCapacity;
    private RoomView roomView;
    private Long floor;
    private Boolean smokingAllowed;

    public RoomResponse(Room room){
        this.uid = room.getUuid();
        this.hotelId = room.getHotel().getUuid();
        this.hotelName = room.getHotel().getName();
        this.name = room.getName();
        this.price = room.getPrice().doubleValue();
        this.timeframe = room.getTimeFrameInHours();
        this.roomType = RoomType.valueOf(room.getRoomType());
        this.maxCapacity = room.getMaxCapacity();
        this.roomView = RoomView.valueOf(room.getRoomView());
        this.floor = room.getFloor();
        this.smokingAllowed = room.getSmokingAllowed();
    }
}
