package com.quyvx.accommodationbooking.service.room;

import com.quyvx.accommodationbooking.dto.RoomDto;
import com.quyvx.accommodationbooking.exception.InvalidException;
import com.quyvx.accommodationbooking.model.Hotel;
import com.quyvx.accommodationbooking.model.Room;
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


    @Override
    public String save(Long idAccount, Long idHotel, RoomDto roomDto) throws InvalidException {
        Optional<Hotel> hotel = hotelService.findById(idHotel);
        if(hotel.isPresent()){
            if(Objects.equals(hotel.get().getAccount().getId(), idAccount)){
                for(int i =0; i< roomDto.getNumberRooms(); i++){
                    Room newRoom = new Room();
                    newRoom.setHotel(hotel.get());
                    newRoom.setRoomType(roomDto.getRoomType());
                    newRoom.setPrice(roomDto.getPrice());
                    newRoom.setDescription(roomDto.getDescription());
                    newRoom.setService(roomDto.getService());
                    newRoom.setImages(roomDto.getImages());
                    roomRepository.save(newRoom);
                }
                return "New rooms have been added!";
            }
            throw new InvalidException("Account with id " +idAccount+ " is not the owner of the hotel with id " + idHotel);
        }
        throw new InvalidException("Hotel is not found for the id " + idHotel);
    }

}
