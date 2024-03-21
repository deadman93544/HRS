package com.hrs.booking.service;

import com.hrs.booking.entity.Hotel;
import com.hrs.booking.jpa.HotelRepository;
import com.hrs.booking.view.PageResponse;
import com.hrs.booking.view.hotel.HotelRequest;
import com.hrs.booking.view.hotel.HotelResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HotelService extends BaseService{

    @Autowired
    private HotelRepository hotelRepository;

    public Hotel createHotel(HotelRequest request) {
        Hotel hotel = new Hotel();
        hotel.setValues(request);
        return hotelRepository.saveAndFlush(hotel);
    }

    public Hotel updateHotel(HotelRequest request) {
        if (request.getUid() != null) {
            Optional<Hotel> optionalHotel = hotelRepository.findByUuid(request.getUid());
            if (optionalHotel.isPresent()) {
                Hotel hotel = optionalHotel.get();
                hotel.setValues(request);
                return hotelRepository.saveAndFlush(hotel);
            } else {
                throw new RuntimeException("Couldn't find Hotel with id : " + request.getUid());
            }
        } else {
            throw new RuntimeException("No Id Found for which Hotel need to be updated!");
        }
    }

    public PageResponse<HotelResponse> getAllHotels(Pageable paging, String searchKey) {
        Page<Hotel> hotels;

        if(searchKey == null || searchKey.isEmpty()) {
            hotels = hotelRepository.findAll(paging);
        } else {
            hotels = hotelRepository.findAllByNameContaining(searchKey, paging);
        }

        PageResponse<HotelResponse> pageResponse= new PageResponse<>();
        pageResponse.setTotalPages(hotels.getTotalPages());
        pageResponse.setPageNumber(hotels.getNumber());
        pageResponse.setPageSize(hotels.getSize());
        pageResponse.setTotalCount(hotels.getTotalElements());
        pageResponse.setList(hotels.stream().map(HotelResponse::new).collect(Collectors.toList()));
        return pageResponse;
    }

    public Hotel getHotelDetails(String id) {
        Optional<Hotel> opHotel = hotelRepository.findByUuid(id);
        if(opHotel.isPresent()){
            return opHotel.get();
        }
        else {
            throw new RuntimeException("Hotel with id: " + id + "not found");
        }
    }

}
