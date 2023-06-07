package com.quyvx.accommodationbooking.service.hotel;

import com.quyvx.accommodationbooking.dto.HotelDetail;
import com.quyvx.accommodationbooking.dto.HotelDto;
import com.quyvx.accommodationbooking.dto.Message;
import com.quyvx.accommodationbooking.dto.RoomDto;
import com.quyvx.accommodationbooking.exception.InvalidException;
import com.quyvx.accommodationbooking.model.Account;
import com.quyvx.accommodationbooking.model.Hotel;
import com.quyvx.accommodationbooking.model.Room;
import com.quyvx.accommodationbooking.repository.HotelRepository;
import com.quyvx.accommodationbooking.repository.RoomRepository;
import com.quyvx.accommodationbooking.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HotelServiceImpl implements HotelService{

    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private RoomRepository roomRepository;

    @Override
    public Message save(Long id, HotelDto hotelDto) {
        Optional<Account> account = accountService.findById(id);

        Hotel hotel = new Hotel();
        hotel.setName(hotelDto.getNameHotel());
        hotel.setLocation(hotelDto.getLocation());
        hotel.setShortDescription(hotelDto.getShortDescription());
        hotel.setDetailDescription(hotelDto.getDetailDescription());
        hotel.setAssess(hotelDto.getAssess());
        hotel.setAvatarHotel(hotelDto.getAvatarHotel());
        hotel.setAccount(account.get());
        hotelRepository.save(hotel);
        return new Message("New hotel added!");
    }

//    @Override
//    public RoomDto viewRoom(Long idHotel) {
//        List<String> rooms = roomRepository.findDistinctRoomTypeByHotelId(idHotel);
//
//        RoomDto tempRoom = new RoomDto();
//        return null;
//    }

    @Override
    public HotelDetail viewHotelDetail(Long idHotel) throws InvalidException {
        List<String> roomType =  roomRepository.findDistinctRoomTypeByHotelId(idHotel);
        Optional<Hotel> hotel = hotelRepository.findById(idHotel);
        if(hotel.isPresent()){
            HotelDetail hotelDetail = new HotelDetail();
            hotelDetail.setNameHotel(hotel.get().getName());
            hotelDetail.setLocation(hotel.get().getLocation());
            hotelDetail.setScore(hotel.get().getScore());
            hotelDetail.setShortDescription(hotel.get().getShortDescription());
            hotelDetail.setDetailDescription(hotel.get().getDetailDescription());
            hotelDetail.setAssess(hotel.get().getAssess());
            hotelDetail.setAvatarHotel(hotel.get().getAvatarHotel());
            hotelDetail.setNumberRating(hotel.get().getNumberRating());
            List<RoomDto> roomDtoList = new ArrayList<>();
            for(String type : roomType){
                List<Room> rooms = roomRepository.findByHotelIdAndRoomType(idHotel, type);
                RoomDto roomDto  = new RoomDto();
                roomDto.setRoomType(type);
                roomDto.setPrice(rooms.get(1).getPrice());
                roomDto.setDescription(rooms.get(1).getDescription());
                roomDto.setService(rooms.get(1).getService());
                roomDto.setImages(rooms.get(1).getImages());
                roomDtoList.add(roomDto);
            }
            hotelDetail.setRooms(roomDtoList);
            return hotelDetail;
        } throw new InvalidException("Hotel is not found for the id " + idHotel);
    }

    @Override
    public List<Hotel> findByAccountId(Long account_id, Pageable pageable) {
        return hotelRepository.findByAccountId(account_id, pageable);
    }

    @Override
    public Optional<Hotel> findById(Long id) throws InvalidException {
        Optional<Hotel> hotel = hotelRepository.findById(id);
        if(hotel.isPresent()){
            return hotel;
        }
        throw new InvalidException("Hotel is not found for the id " + id);
    }


    @Override
    public Page<Hotel> findAll(Pageable pageable) {
        return hotelRepository.findAll(pageable);
    }
}
