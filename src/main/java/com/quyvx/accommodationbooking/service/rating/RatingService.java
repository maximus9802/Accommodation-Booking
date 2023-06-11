package com.quyvx.accommodationbooking.service.rating;

import com.quyvx.accommodationbooking.dto.Message;
import com.quyvx.accommodationbooking.dto.RatingDto;

public interface RatingService {
    Message newRating(Long idAccount, Long roomId, Long idBooking, RatingDto ratingDto) throws Exception;
}
