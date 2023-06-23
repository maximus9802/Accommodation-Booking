package com.quyvx.accommodationbooking.service.rating;

import com.quyvx.accommodationbooking.dto.Message;
import com.quyvx.accommodationbooking.dto.NotificationDto;
import com.quyvx.accommodationbooking.dto.RatingDto;
import com.quyvx.accommodationbooking.exception.InvalidException;
import com.quyvx.accommodationbooking.model.*;
import com.quyvx.accommodationbooking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    @Autowired
    private AccountRepository accountRepository;
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
                    if(currentNumberRating == 1 ){
                        room.setScore(0);
                        room.setNumberRating(0);
                    }
                    else {
                        room.setScore(newScore);
                        room.setNumberRating(currentNumberRating - 1 );
                    }


                    currentScore = hotel.getScore();
                    currentNumberRating = hotel.getNumberRating();
                    newScore = (currentScore*currentNumberRating - rating.getScore()) / (currentNumberRating - 1);

                    if(currentNumberRating == 1){
                        hotel.setScore(0);
                        hotel.setNumberRating(0);
                    }
                    else {
                        hotel.setScore(newScore);
                        hotel.setNumberRating(currentNumberRating - 1);
                    }

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

    @Override
    public List<RatingDto> getAllRatingByAccountId(Long accountId) throws Exception {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if(accountOptional.isPresent()){
            List<Booking> bookings = bookingRepository.findByAccountId(accountId);
            List<RatingDto> ratingDtos = new ArrayList<>();
            for(Booking booking : bookings){
                List<Rating> ratings = ratingRepository.findByBookingId(booking.getId());
                if(ratings.isEmpty()) continue;
                else {
                    for(Rating rating : ratings){
                        var temp = RatingDto
                                .builder()
                                .comment(rating.getComment())
                                .score(rating.getScore())
                                .roomType(rating.getRoom().getRoomType())
                                .nameHotel(rating.getRoom().getHotel().getName())
                                .build();
                        ratingDtos.add(temp);
                    }
                }
            }
            return ratingDtos;
        } throw new Exception("Invalid account");
    }

    @Override
    public List<RatingDto> getAllRatingByHotelId(Long hotelId) throws Exception {
        Optional<Hotel> hotelOptional = hotelRepository.findById(hotelId);
        if(hotelOptional.isPresent()){
            List<Room> rooms = roomRepository.findByHotelId(hotelId);
            List<RatingDto> ratingDtos = new ArrayList<>();
            for(Room room : rooms){
                List<Rating> ratings = ratingRepository.findByRoomId(room.getId());
                for(Rating rating : ratings){
                    var temp = RatingDto
                            .builder()
                            .nameHotel(hotelOptional.get().getName())
                            .roomType(room.getRoomType())
                            .comment(rating.getComment())
                            .score(rating.getScore())
                            .build();
                    ratingDtos.add(temp);
                }
            }
            return ratingDtos;
        } throw new InvalidException("Invalid hotel");
    }


    @Override
    public NotificationDto updateRating(Long idAccount, Long idBooking, Long idRating, RatingDto ratingDto) throws Exception {
        Optional<Booking> bookingOptional = bookingRepository.findById(idBooking);
        if(bookingOptional.isPresent()){
            Booking booking = bookingOptional.get();
            if(booking.getAccount().getId() == idAccount){
                Optional<Rating> ratingOptional = ratingRepository.findById(idRating);
                if(ratingOptional.isPresent()){
                    Rating rating = ratingOptional.get();
                    rating.setId(idRating);
                    Optional<Room> roomOptional = roomRepository.findById(rating.getRoom().getId());
                    Room room = roomOptional.get();
                    Hotel hotel = room.getHotel();

                    float currentScore = room.getScore();
                    int currentNumberRating = room.getNumberRating();
                    float newScore = (currentScore*currentNumberRating - rating.getScore() + ratingDto.getScore())/currentNumberRating;
                    room.setScore(newScore);

                    currentScore = hotel.getScore();
                    currentNumberRating = hotel.getNumberRating();
                    newScore = (currentScore*currentNumberRating - rating.getScore() + ratingDto.getScore())/currentNumberRating;
                    hotel.setScore(newScore);

                    rating.setScore(ratingDto.getScore());
                    rating.setComment(rating.getComment());

                    roomRepository.save(room);
                    hotelRepository.save(hotel);

                    Rating temp = ratingRepository.save(rating);
                    var noti = Notification
                            .builder()
                            .rating(temp)
                            .message("Update rating successful!")
                            .account(booking.getAccount())
                            .build();
                    return NotificationDto
                            .builder()
                            .accountId(booking.getAccount().getId())
                            .message("Update rating successful!")
                            .ratingId(temp.getId())
                            .build();
                } throw new Exception("Invalid Rating");
            } throw new Exception("This is not your booking!");
        } throw new Exception("Invalid Booking");
    }


}
