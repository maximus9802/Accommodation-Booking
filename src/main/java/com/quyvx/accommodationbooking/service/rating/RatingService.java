package com.quyvx.accommodationbooking.service.rating;

import com.quyvx.accommodationbooking.dto.Message;
import com.quyvx.accommodationbooking.dto.NotificationDto;
import com.quyvx.accommodationbooking.dto.RatingDto;

import java.util.List;

public interface RatingService {
    NotificationDto newRating(Long idAccount, Long roomId, Long idBooking, RatingDto ratingDto) throws Exception;

    NotificationDto deleteRating(Long idAccount, Long idBooking, Long idRating) throws Exception;

    List<RatingDto> getAllRatingByAccountId(Long accountId) throws  Exception;

    List<RatingDto> getAllRatingByHotelId(Long hotelId) throws Exception;
    NotificationDto updateRating(Long idAccount, Long idBooking, Long idRating, RatingDto ratingDto) throws Exception;
}
