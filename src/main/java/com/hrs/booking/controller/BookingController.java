package com.hrs.booking.controller;

import com.hrs.booking.service.BookingService;
import com.hrs.booking.view.PageResponse;
import com.hrs.booking.view.booking.BookingRequest;
import com.hrs.booking.view.booking.BookingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static com.hrs.booking.auth.HRSAuthorisations.Privileges.BOOKING_READ;
import static com.hrs.booking.auth.HRSAuthorisations.Privileges.BOOKING_WRITE;

//API Controller class to handle endpoints related to Booking

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    //    API Endpoint to create a booking
    @PostMapping
    @Secured(BOOKING_WRITE)
    public BookingResponse createBooking(@RequestBody BookingRequest request){
        return new BookingResponse(bookingService.createBooking(request));
    }

    //    API Endpoint to Get Booking By Id
    @GetMapping
    @Secured(BOOKING_READ)
    public BookingResponse getBookingById(@RequestParam String id){
        return new BookingResponse(bookingService.getBookingById(id));
    }

    //    API Endpoint to Get All the bookings
    @GetMapping("/list")
    @Secured(BOOKING_READ)
    public PageResponse<BookingResponse> getAllBookingsForUser(
            @SortDefault.SortDefaults({@SortDefault(sort = "name", direction = Sort.Direction.DESC)})
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable paging = PageRequest.of(page, size);
        return bookingService.getAllBookingsForUser(paging);
    }

    //    API Endpoint to Cancel the booking...
    @PutMapping("/cancel")
    @Secured(BOOKING_WRITE)
    public void cancelBooking(@RequestParam String id){
        bookingService.cancelBooking(id);
    }
}
