package com.quyvx.accommodationbooking.service.rating;

import com.quyvx.accommodationbooking.dto.Message;
import com.quyvx.accommodationbooking.dto.RatingDto;
import com.quyvx.accommodationbooking.model.*;
import com.quyvx.accommodationbooking.repository.BookingRepository;
import com.quyvx.accommodationbooking.repository.RatingRepository;
import com.quyvx.accommodationbooking.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RatingServiceImpl implements  RatingService{
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Override
    public Message newRating(Long idAccount, Long roomId, Long idBooking, RatingDto ratingDto) throws Exception {
        Optional<Booking> bookingOptional = bookingRepository.findById(idBooking);
        if(bookingOptional.isPresent()){
            Booking booking = bookingOptional.get();
            if(booking.getAccount().getId() == idAccount){
                Optional<Room> roomOptional = roomRepository.findById(roomId);
                if(roomOptional.isPresent()){
                    Room room = roomOptional.get();
                    Rating rating = new Rating();

                    rating.setBooking(booking);
                    rating.setRoom(room);
                    rating.setComment(ratingDto.getComment());
                    rating.setScore(ratingDto.getScore());

                    room.setScore((room.getScore()*room.getNumberRating() + ratingDto.getScore())/(room.getNumberRating() +1));
                    room.setNumberRating(room.getNumberRating() +1 );

                    roomRepository.save(room);
                    ratingRepository.save(rating);
                    return new Message("Rating successful!");
                } throw new Exception("Invalid Room");
            } throw new Exception("This is not your booking!");
        } throw new Exception("Invalid Booking");
    }
}
