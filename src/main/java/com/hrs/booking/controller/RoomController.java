package com.hrs.booking.controller;

import com.hrs.booking.service.RoomService;
import com.hrs.booking.view.PageResponse;
import com.hrs.booking.view.room.RoomRequest;
import com.hrs.booking.view.room.RoomResponse;
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
@RequestMapping("/api/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping
    @Secured(HOTEL_WRITE)
    public RoomResponse createRoom(@RequestBody RoomRequest request) {
        return new RoomResponse(roomService.createRoom(request));
    }

    @PutMapping
    @Secured(HOTEL_WRITE)
    public RoomResponse updateRoom(@RequestBody RoomRequest request) {
        return new RoomResponse(roomService.updateRoom(request));
    }

    @GetMapping("/list")
    @Secured(HOTEL_READ)
    public PageResponse<RoomResponse> getRoomsBySearch(
            @SortDefault.SortDefaults({@SortDefault(sort = "name", direction = Sort.Direction.DESC)})
            @RequestParam(required = false) String searchKey,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable paging = PageRequest.of(page, size);
        return roomService.getAllRooms(paging, searchKey);
    }

    @GetMapping
    @Secured(HOTEL_READ)
    public RoomResponse getRoomDetails(@RequestParam String id) {
        return new RoomResponse(roomService.getRoomDetails(id));
    }
}
