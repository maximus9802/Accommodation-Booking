package com.quyvx.accommodationbooking.service.booking;

import com.quyvx.accommodationbooking.dto.BookingDto;
import com.quyvx.accommodationbooking.dto.Message;
import com.quyvx.accommodationbooking.dto.NotificationDto;
import com.quyvx.accommodationbooking.exception.InvalidException;
import com.quyvx.accommodationbooking.model.Booking;

import java.util.List;

public interface BookingService {

    NotificationDto newBooking(Long id, BookingDto bookingDto) throws InvalidException;

    List<BookingDto> allBooking(Long accountId);

    List<BookingDto> searchByPhoneCustomer(String phoneCustomer, Long hotelId) throws InvalidException;

}
