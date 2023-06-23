package com.quyvx.accommodationbooking.service.room;

import com.quyvx.accommodationbooking.dto.NotificationDto;
import com.quyvx.accommodationbooking.dto.RoomDto;
import com.quyvx.accommodationbooking.exception.InvalidException;
import com.quyvx.accommodationbooking.model.Booking;
import com.quyvx.accommodationbooking.model.Hotel;
import com.quyvx.accommodationbooking.model.Notification;
import com.quyvx.accommodationbooking.model.Room;
import com.quyvx.accommodationbooking.repository.BookingRepository;
import com.quyvx.accommodationbooking.repository.NotificationRepository;
import com.quyvx.accommodationbooking.repository.RoomRepository;
import com.quyvx.accommodationbooking.service.hotel.HotelService;
import com.quyvx.accommodationbooking.service.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private NotificationRepository notificationRepository;


    @Override
    public NotificationDto saveNewRoom(Long idAccount, Long idHotel, RoomDto roomDto) throws InvalidException {
        Optional<Hotel> hotel = hotelService.findById(idHotel);
        if(hotel.isPresent()){
            if(Objects.equals(hotel.get().getAccount().getId(), idAccount)){
                for(int i =0; i< roomDto.getNumberRooms(); i++){
                    Room room = new Room();
                    room.setHotel(hotel.get());
                    room.setRoomType(roomDto.getRoomType());
                    room.setPrice(roomDto.getPrice());
                    room.setDescription(roomDto.getDescription());
                    room.setService(roomDto.getService());
                    room.setImages(roomDto.getImages());
                    roomRepository.save(room);
                }
                var noti = Notification
                        .builder()
                        .account(hotel.get().getAccount())
                        .hotel(hotel.get())
                        .message("New rooms have been added!")
                        .build();
                notificationRepository.save(noti);
                return NotificationDto
                        .builder()
                        .message("New rooms have been added!")
                        .hotelId(hotel.get().getId())
                        .accountId(idAccount)
                        .build();
            }
            throw new InvalidException("Account with id " +idAccount+ " is not the owner of the hotel with id " + idHotel);
        }
        throw new InvalidException("Hotel is not found for the id " + idHotel);
    }

    @Override
    public boolean isBookingInHotel(Long bookingId, Long hotelId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        Booking booking = optionalBooking.orElse(null);

        if (booking != null) {
            Hotel hotel = booking.getRooms().stream()
                    .findFirst()
                    .map(Room::getHotel)
                    .orElse(null);

            if (hotel != null && hotel.getId().equals(hotelId)) {
                return true;
            }
        }

        return false;
    }

}
