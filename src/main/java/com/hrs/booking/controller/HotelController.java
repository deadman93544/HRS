package com.hrs.booking.controller;

import com.hrs.booking.service.HotelService;
import com.hrs.booking.view.PageResponse;
import com.hrs.booking.view.hotel.HotelRequest;
import com.hrs.booking.view.hotel.HotelResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static com.hrs.booking.auth.HRSAuthorisations.Privileges.HOTEL_READ;
import static com.hrs.booking.auth.HRSAuthorisations.Privileges.HOTEL_WRITE;

@RestController
@RequestMapping("/api/hotel")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @PostMapping
    @Secured(HOTEL_WRITE)
    public HotelResponse createHotel(@RequestBody HotelRequest request) {
        return new HotelResponse(hotelService.createHotel(request));
    }

    @PutMapping
    @Secured(HOTEL_WRITE)
    public HotelResponse updateHotel(@RequestBody HotelRequest request) {
        return new HotelResponse(hotelService.updateHotel(request));
    }

    @GetMapping("/list")
    @Secured(HOTEL_READ)
    public PageResponse<HotelResponse> getHotelsBySearch(
            @SortDefault.SortDefaults({@SortDefault(sort = "name", direction = Sort.Direction.DESC)})
            @RequestParam(required = false) String searchKey,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable paging = PageRequest.of(page, size);
        return hotelService.getAllHotels(paging, searchKey);
    }

    @GetMapping
    @Secured(HOTEL_READ)
    public HotelResponse getHotelDetails(@RequestParam String id) {
        return new HotelResponse(hotelService.getHotelDetails(id));
    }
}
