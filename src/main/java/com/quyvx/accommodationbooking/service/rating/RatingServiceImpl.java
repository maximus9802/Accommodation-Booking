package com.quyvx.accommodationbooking.service.rating;

import com.quyvx.accommodationbooking.dto.Message;
import com.quyvx.accommodationbooking.dto.RatingDto;
import com.quyvx.accommodationbooking.model.*;
import com.quyvx.accommodationbooking.repository.BookingRepository;
import com.quyvx.accommodationbooking.repository.HotelRepository;
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
    @Autowired
    private HotelRepository hotelRepository;
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
                    Hotel hotel = room.getHotel();

                    rating.setBooking(booking);
                    rating.setRoom(room);
                    rating.setComment(ratingDto.getComment());
                    rating.setScore(ratingDto.getScore());

                    float currentScore = room.getScore();
                    int currentNumberRating = room.getNumberRating();
                    float newScore = (currentScore*currentNumberRating + ratingDto.getScore()) / (currentNumberRating + 1);

                    room.setScore(newScore);
                    room.setNumberRating(currentNumberRating +1 );

                    currentScore = hotel.getScore();
                    currentNumberRating = hotel.getNumberRating();
                    newScore = (currentScore*currentNumberRating + ratingDto.getScore()) / (currentNumberRating + 1);

                    hotel.setScore(newScore);
                    hotel.setNumberRating(currentNumberRating + 1);

                    roomRepository.save(room);
                    hotelRepository.save(hotel);
                    ratingRepository.save(rating);
                    return new Message("Rating successful!");
                } throw new Exception("Invalid Room");
            } throw new Exception("This is not your booking!");
        } throw new Exception("Invalid Booking");
    }

    @Override
    public Message deleteRating(Long idAccount, Long idBooking, Long idRating) throws Exception {
        Optional<Booking> bookingOptional = bookingRepository.findById(idBooking);
        if(bookingOptional.isPresent()){
            Booking booking = bookingOptional.get();
            if(booking.getAccount().getId() == idAccount){
                Optional<Rating> ratingOptional = ratingRepository.findById(idRating);
                if(ratingOptional.isPresent()){
                    Rating rating = ratingOptional.get();
                    Room room = rating.getRoom();
                    Hotel hotel = room.getHotel();

                    float currentScore = room.getScore();
                    int currentNumberRating = room.getNumberRating();
                    float newScore = (currentScore*currentNumberRating - rating.getScore()) / (currentNumberRating - 1);

                    room.setScore(newScore);
                    room.setNumberRating(currentNumberRating - 1 );

                    currentScore = hotel.getScore();
                    currentNumberRating = hotel.getNumberRating();
                    newScore = (currentScore*currentNumberRating - rating.getScore()) / (currentNumberRating - 1);


                    hotel.setScore(newScore);
                    hotel.setNumberRating(currentNumberRating - 1);

//                    hotel.setScore((hotel.getScore()*(float)hotel.getNumberRating() - (float)rating.getScore())/((float)hotel.getNumberRating() - 1));
//                    hotel.setNumberRating(hotel.getNumberRating() - 1);
//
//                    room.setScore((room.getScore()*(float)room.getNumberRating() - (float)rating.getScore())/((float)room.getNumberRating() - 1));
//                    room.setNumberRating(room.getNumberRating() - 1);

                    roomRepository.save(room);
                    hotelRepository.save(hotel);
                    ratingRepository.deleteById(idRating);
                    return new Message("Delete successful!");
                } throw new Exception("Invalid Rating.");
            } throw new Exception("This is not your booking.");
        } throw new Exception("Invalid Booking.");

    }


}
