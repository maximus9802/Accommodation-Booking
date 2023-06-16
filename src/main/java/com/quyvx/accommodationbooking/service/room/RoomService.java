package com.quyvx.accommodationbooking.service.room;

import com.quyvx.accommodationbooking.dto.NotificationDto;
import com.quyvx.accommodationbooking.dto.RoomDto;
import com.quyvx.accommodationbooking.exception.InvalidException;
import com.quyvx.accommodationbooking.model.Room;


import java.util.List;

public interface RoomService {
    NotificationDto save(Long idAccount, Long idHotel, RoomDto roomDto) throws InvalidException;

    boolean isBookingInHotel(Long bookingId, Long hotelId);
}
