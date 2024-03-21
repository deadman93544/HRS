package com.hrs.booking.service;

import com.hrs.booking.entity.Booking;
import com.hrs.booking.entity.HRSUser;
import com.hrs.booking.entity.Room;
import com.hrs.booking.enums.BookingStatus;
import com.hrs.booking.jpa.BookingRepository;
import com.hrs.booking.jpa.RoomRepository;
import com.hrs.booking.view.PageResponse;
import com.hrs.booking.view.booking.BookingRequest;
import com.hrs.booking.view.booking.BookingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.stream.Collectors;

// Service class implementing Booking related Business logic.
@Service
public class BookingService extends BaseService{

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

//    Method to create a new Booking.
    public Booking createBooking(BookingRequest request) {
        Booking booking = new Booking();
        booking.setValues(request);

        Room room = roomRepository.findByUuid(request.getRoomId()).orElseThrow(() -> new EntityNotFoundException("Room not found"));
        booking.setRoom(room);
        booking.setPrice(room.getPrice());

        long overlapping = bookingRepository.countOverlappingBookings(room.getId(), booking.getCheckIn(), booking.getCheckOut());
        if(overlapping > 0) booking.setBookingStatus(BookingStatus.PENDING.toString());
        else booking.setBookingStatus(BookingStatus.CONFIRMED.toString());

        return bookingRepository.saveAndFlush(booking);
    }

//    Method to get a Page of Bookings of the Authenticated User wrapped in a PageResponse class.
    public PageResponse<BookingResponse> getAllBookingsForUser(Pageable paging) {
        HRSUser user = getCurrentUser();

        Page<Booking> bookings = bookingRepository.findAllByCreatedBy(user, paging);

        PageResponse<BookingResponse> pageResponse= new PageResponse<>();
        pageResponse.setTotalPages(bookings.getTotalPages());
        pageResponse.setPageNumber(bookings.getNumber());
        pageResponse.setPageSize(bookings.getSize());
        pageResponse.setTotalCount(bookings.getTotalElements());
        pageResponse.setList(bookings.stream().map(BookingResponse::new).collect(Collectors.toList()));
        return pageResponse;
    }

//    Method to get a Booking By Id.
    public Booking getBookingById(String id) {
        Booking booking = bookingRepository.findByUuid(id).orElseThrow(() -> new EntityNotFoundException("Room not found"));
        if(!booking.getCreatedBy().get().getUuid().equals(getCurrentUser().getUuid())){
            throw new RuntimeException("You don't have access to this Booking!");
        }
        return booking;

    }

//    Method to cancel a Booking.
    public void cancelBooking(String id) {
        Booking booking = getBookingById(id);
        boolean confirmed = booking.getBookingStatus().equals(BookingStatus.CONFIRMED.toString());
        booking.setBookingStatus(BookingStatus.CANCELLED.toString());
        bookingRepository.saveAndFlush(booking);

        if(!confirmed) return;
        // TODO: to check other pending bookings and confirm which are in the time slot...
    }
}
