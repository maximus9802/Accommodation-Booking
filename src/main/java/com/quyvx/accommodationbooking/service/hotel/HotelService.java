package com.quyvx.accommodationbooking.service.hotel;

import com.quyvx.accommodationbooking.dto.*;
import com.quyvx.accommodationbooking.exception.InvalidException;
import com.quyvx.accommodationbooking.model.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface HotelService {

    NotificationDto save(Long id, HotelDto hotelDto) throws Exception;

    HotelDetail viewHotelDetail(Long idHotel) throws InvalidException;

    HotelDetail viewHotelDetail(Long idHotel, DateTemp dateTemp) throws InvalidException;

    List<Hotel> findByAccountId(Long account_id, Pageable pageable);

    Optional<Hotel> findById(Long id) throws InvalidException;

    Page<Hotel> findAll(Pageable pageable);

    List<HotelDto> searchByLocation(Integer pageNumber, Integer pageSize, String location, String sortBy);

    NotificationDto updateHotelInfo(Long idAccount, Long idHotel, HotelDto hotelDto) throws  Exception;
}
