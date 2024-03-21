package com.hrs.booking.service;

import com.hrs.booking.entity.Hotel;
import com.hrs.booking.entity.Room;
import com.hrs.booking.jpa.HotelRepository;
import com.hrs.booking.jpa.RoomRepository;
import com.hrs.booking.view.PageResponse;
import com.hrs.booking.view.room.RoomRequest;
import com.hrs.booking.view.room.RoomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomService extends BaseService{

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelRepository hotelRepository;

    public Room createRoom(RoomRequest request) {
        Room room = new Room();
        room.setValues(request);
        Optional<Hotel> optionalHotel = hotelRepository.findByUuid(request.getHotelId());
        if(optionalHotel.isEmpty()) throw new RuntimeException("No Hotel found with given Id: " + request.getHotelId());
        room.setHotel(optionalHotel.get());
        return roomRepository.saveAndFlush(room);
    }

    public Room updateRoom(RoomRequest request) {
        if (request.getUid() != null) {
            Optional<Room> optRoom = roomRepository.findByUuid(request.getUid());
            if (optRoom.isPresent()) {
                Room room = optRoom.get();
                room.setValues(request);
                return roomRepository.saveAndFlush(room);
            } else {
                throw new RuntimeException("Couldn't find Room with id : " + request.getUid());
            }
        } else {
            throw new RuntimeException("No Id Found for which Room need to be updated!");
        }
    }

    public PageResponse<RoomResponse> getAllRooms(Pageable paging, String searchKey) {
        Page<Room> rooms;

        if(searchKey == null || searchKey.isEmpty()) {
            rooms = roomRepository.findAll(paging);
        } else {
            List<Hotel> hotels = hotelRepository.findAllByNameContaining(searchKey);
            rooms = roomRepository.findAllByHotelIn(hotels, paging);
        }

        PageResponse<RoomResponse> pageResponse= new PageResponse<>();
        pageResponse.setTotalPages(rooms.getTotalPages());
        pageResponse.setPageNumber(rooms.getNumber());
        pageResponse.setPageSize(rooms.getSize());
        pageResponse.setTotalCount(rooms.getTotalElements());
        pageResponse.setList(rooms.stream().map(RoomResponse::new).collect(Collectors.toList()));
        return pageResponse;
    }

    public Room getRoomDetails(String id) {
        Optional<Room> opRoom = roomRepository.findByUuid(id);
        if(opRoom.isPresent()){
            return opRoom.get();
        }
        else {
            throw new RuntimeException("Room with id: " + id + "not found");
        }
    }
}
