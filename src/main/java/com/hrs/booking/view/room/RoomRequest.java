package com.hrs.booking.view.room;

import com.hrs.booking.enums.RoomType;
import com.hrs.booking.enums.RoomView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomRequest {
    private String uid;
    private String hotelId;
    private String name;
    private Double price;
    private Long timeframe;
    private RoomType roomType;
    private Long maxCapacity;
    private RoomView roomView;
    private Long floor;
    private Boolean smokingAllowed;
}
