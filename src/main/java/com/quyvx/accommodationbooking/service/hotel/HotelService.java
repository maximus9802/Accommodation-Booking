package com.quyvx.accommodationbooking.service.hotel;

import com.quyvx.accommodationbooking.dto.HotelDetail;
import com.quyvx.accommodationbooking.dto.HotelDto;
import com.quyvx.accommodationbooking.dto.Message;
import com.quyvx.accommodationbooking.exception.InvalidException;
import com.quyvx.accommodationbooking.model.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface HotelService {

    Message save(Long id, HotelDto hotelDto);

    HotelDetail viewHotelDetail(Long idHotel) throws InvalidException;

    List<Hotel> findByAccountId(Long account_id, Pageable pageable);

    Optional<Hotel> findById(Long id) throws InvalidException;

    Page<Hotel> findAll(Pageable pageable);
}
