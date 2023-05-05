package com.quyvx.accommodationbooking.service.hotel;

import com.quyvx.accommodationbooking.dto.HotelDto;
import com.quyvx.accommodationbooking.model.Hotel;
import com.quyvx.accommodationbooking.repository.HotelRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelServiceImpl implements HotelService{

    @Autowired
    private HotelRepository hotelRepository;

}
