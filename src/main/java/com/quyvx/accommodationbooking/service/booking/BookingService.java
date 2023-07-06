package com.quyvx.accommodationbooking.service.booking;

import com.quyvx.accommodationbooking.dto.BookingDto;
import com.quyvx.accommodationbooking.dto.Message;
import com.quyvx.accommodationbooking.dto.NotificationDto;
import com.quyvx.accommodationbooking.exception.InvalidException;
import com.quyvx.accommodationbooking.model.Booking;
import com.quyvx.accommodationbooking.model.Notification;

import java.util.Date;
import java.util.List;

public interface BookingService {

    NotificationDto newBooking(Long id, BookingDto bookingDto) throws Exception;

    List<BookingDto> allBooking(Long accountId);

    List<BookingDto> getAllBookingByOwner(Long idAccount, Long idHotel) throws  Exception;

    List<BookingDto> searchByPhoneCustomer(String phoneCustomer, Long hotelId) throws InvalidException;

    NotificationDto changeStatus(Long idAccount, Long idHotel, Long idBooking, String status) throws Exception;

    NotificationDto cancelBookingByCustomer(Long idAccount, Long idBooking, String reason) throws  Exception;

    NotificationDto cancelBookingByOwner(Long idAccount, Long idHotel, Long idBooking, String reason) throws Exception;

    BookingDto searchBookingById(Long idCustomer, Long idBooking) throws Exception;

    BookingDto searchBookingById(Long idOwner, Long idHotel, Long idBooking) throws Exception;

    boolean checkDateBefore(Date date);

    boolean checkDateAfter(Date date);
}
