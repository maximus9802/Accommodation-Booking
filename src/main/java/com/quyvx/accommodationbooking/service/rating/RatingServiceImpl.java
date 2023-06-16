package com.quyvx.accommodationbooking.service.rating;

import com.quyvx.accommodationbooking.dto.Message;
import com.quyvx.accommodationbooking.dto.NotificationDto;
import com.quyvx.accommodationbooking.dto.RatingDto;
import com.quyvx.accommodationbooking.model.*;
import com.quyvx.accommodationbooking.repository.*;
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
    @Autowired
    private NotificationRepository notificationRepository;
    @Override
    public NotificationDto newRating(Long idAccount, Long roomId, Long idBooking, RatingDto ratingDto) throws Exception {
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

                    Rating temp = ratingRepository.save(rating);
                    var noti = Notification
                            .builder()
                            .message("Rating successful!")
                            .account(booking.getAccount())
                            .rating(temp)
                            .build();
                    notificationRepository.save(noti);

                    return NotificationDto
                            .builder()
                            .accountId(idAccount)
                            .message("Rating successful!")
                            .ratingId(temp.getId())
                            .build();
                } throw new Exception("Invalid Room");
            } throw new Exception("This is not your booking!");
        } throw new Exception("Invalid Booking");
    }

    @Override
    public NotificationDto deleteRating(Long idAccount, Long idBooking, Long idRating) throws Exception {
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
                    var noti = Notification
                            .builder()
                            .message("Delete rating successful!")
                            .account(booking.getAccount())
                            .build();
                    notificationRepository.save(noti);
                    return NotificationDto
                            .builder()
                            .message("Delete rating successful!")
                            .accountId(idAccount)
                            .build();
                } throw new Exception("Invalid rating.");
            } throw new Exception("This is not your booking.");
        } throw new Exception("Invalid Booking.");

    }


}
