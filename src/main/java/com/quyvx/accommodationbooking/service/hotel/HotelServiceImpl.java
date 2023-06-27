package com.quyvx.accommodationbooking.service.hotel;

import com.quyvx.accommodationbooking.dto.*;
import com.quyvx.accommodationbooking.exception.InvalidException;
import com.quyvx.accommodationbooking.model.*;
import com.quyvx.accommodationbooking.repository.DateRentRepository;
import com.quyvx.accommodationbooking.repository.HotelRepository;
import com.quyvx.accommodationbooking.repository.NotificationRepository;
import com.quyvx.accommodationbooking.repository.RoomRepository;
import com.quyvx.accommodationbooking.service.account.AccountService;
import com.quyvx.accommodationbooking.service.daterent.DateRentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HotelServiceImpl implements HotelService{

    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private DateRentService dateRentService;

    @Override
    public NotificationDto save(Long id, HotelDto hotelDto) throws Exception {
        Optional<Account> account = accountService.findById(id);

        if(account.isPresent()){
            if(account.get().getRole() == Role.valueOf("OWNER")){
                Hotel hotel = new Hotel();
                hotel.setName(hotelDto.getNameHotel());
                hotel.setLocation(hotelDto.getLocation());
                hotel.setShortDescription(hotelDto.getShortDescription());
                hotel.setDetailDescription(hotelDto.getDetailDescription());
                hotel.setAssess(hotelDto.getAssess());
                hotel.setAvatarHotel(hotelDto.getAvatarHotel());
                hotel.setAccount(account.get());

                Hotel temp = hotelRepository.save(hotel);

                var noti = Notification
                        .builder()
                        .message("You have successfully added hotel " + hotelDto.getNameHotel())
                        .account(account.get())
                        .hotel(temp)
                        .build();
                notificationRepository.save(noti);
                return NotificationDto
                        .builder()
                        .hotelId(temp.getId())
                        .message("You have successfully added hotel " + hotelDto.getNameHotel())
                        .accountId(id)
                        .build();
            } throw new Exception("You do not have access!");
        } throw new InvalidException("Account is not found" + id);
    }

//    @Override
//    public RoomDto viewRoom(Long idHotel) {
//        List<String> rooms = roomRepository.findDistinctRoomTypeByHotelId(idHotel);
//
//        RoomDto tempRoom = new RoomDto();
//        return null;
//    }

    @Override
    public HotelDetail viewHotelDetail(Long idHotel) throws InvalidException { //this method use for guest
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
                roomDto.setPrice(rooms.get(0).getPrice());
                roomDto.setDescription(rooms.get(0).getDescription());
                roomDto.setService(rooms.get(0).getService());
                roomDto.setImages(rooms.get(0).getImages());
                roomDtoList.add(roomDto);
            }
            hotelDetail.setRooms(roomDtoList);
            return hotelDetail;
        } throw new InvalidException("Hotel is not found for the id " + idHotel);
    }

    @Override
    public HotelDetail viewHotelDetail(Long idHotel, DateTemp dateTemp) throws InvalidException {
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
            List<RoomDto> roomDtos = new ArrayList<>();
            Map<String, Integer> map = new HashMap<>();
            for(String type : roomType){
                List<Room> rooms = roomRepository.findByHotelIdAndRoomType(idHotel, type);
                RoomDto roomDto  = new RoomDto();
                roomDto.setRoomType(type);
                roomDto.setPrice(rooms.get(0).getPrice());
                roomDto.setDescription(rooms.get(0).getDescription());
                roomDto.setService(rooms.get(0).getService());
                roomDto.setImages(rooms.get(0).getImages());

                Iterator<Room> iterator = rooms.iterator();
                while (iterator.hasNext()) {
                    Room room = iterator.next();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateTemp.getCheckIn());
                    while (!calendar.getTime().after(dateTemp.getCheckOut())){
                        Date currentDate = calendar.getTime();
                        if (dateRentService.checkFree(room.getId(), currentDate))
                            calendar.add(Calendar.DATE, 1);
                        else {
                            iterator.remove(); // Sử dụng iterator để xóa phần tử
                            break;
                        }
                    }
                }
                roomDtos.add(roomDto);
                map.put(type, rooms.size());
            }
            hotelDetail.setRooms(roomDtos);
            hotelDetail.setRoomNumber(map);
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

    @Override
    public List<HotelDto> searchByLocation(Integer pageNumber, Integer pageSize, String location, String sortBy) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        Page<Hotel> hotels = hotelRepository.findByLocation(location, pageable);
        List<HotelDto> hotelDtos = new ArrayList<>();
        for(Hotel hotel : hotels){
            var temp = HotelDto
                    .builder()
                    .id(hotel.getId())
                    .nameHotel(hotel.getName())
                    .location(hotel.getLocation())
                    .avatarHotel(hotel.getAvatarHotel())
                    .score(hotel.getScore())
                    .assess(hotel.getAssess())
                    .numberRating(hotel.getNumberRating())
                    .shortDescription(hotel.getShortDescription())
                    .detailDescription(hotel.getDetailDescription())
                    .build();
            hotelDtos.add(temp);
        }
        return hotelDtos;
    }
}
