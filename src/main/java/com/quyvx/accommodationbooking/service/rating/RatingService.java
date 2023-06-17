package com.quyvx.accommodationbooking.service.rating;

import com.quyvx.accommodationbooking.dto.Message;
import com.quyvx.accommodationbooking.dto.NotificationDto;
import com.quyvx.accommodationbooking.dto.RatingDto;

public interface RatingService {
    NotificationDto newRating(Long idAccount, Long roomId, Long idBooking, RatingDto ratingDto) throws Exception;

    NotificationDto deleteRating(Long idAccount, Long idBooking, Long idRating) throws Exception;

    NotificationDto updateRating(Long idAccount, Long idBooking, Long idRating, RatingDto ratingDto) throws Exception;
}
