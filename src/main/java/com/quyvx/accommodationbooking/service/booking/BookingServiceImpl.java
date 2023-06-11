package com.quyvx.accommodationbooking.service.booking;

import com.quyvx.accommodationbooking.dto.BookingDto;
import com.quyvx.accommodationbooking.dto.Message;
import com.quyvx.accommodationbooking.exception.InvalidException;
import com.quyvx.accommodationbooking.model.*;
import com.quyvx.accommodationbooking.repository.*;
import com.quyvx.accommodationbooking.service.daterent.DateRentService;
import com.quyvx.accommodationbooking.service.room.RoomService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookingServiceImpl implements  BookingService{

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private DateRentRepository dateRentRepository;
    @Autowired
    private DateRentService dateRentService;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private RoomService roomService;

    @Override
    public Message newBooking(Long id, BookingDto bookingDto) throws InvalidException {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if(optionalAccount.isPresent()){
            Account account = optionalAccount.get();
            Booking booking = new Booking();
            List<DateRent> dateRents = new ArrayList<>();
            for(String roomType : bookingDto.getRooms().keySet()){
                List<Room> rooms = roomRepository.findByHotelIdAndRoomType(bookingDto.getHotelId(), roomType);
                if(rooms.isEmpty()){
                    return new Message("No room type " + roomType);
                }

                Iterator<Room> iterator = rooms.iterator();
                while (iterator.hasNext()) {
                    Room room = iterator.next();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(bookingDto.getDateCheckIn());
                    while (!calendar.getTime().after(bookingDto.getDateCheckOut())) {
                        Date currentDate = calendar.getTime();
                        if (dateRentService.checkFree(room.getId(), currentDate))
                            calendar.add(Calendar.DATE, 1);
                        else {
                            iterator.remove(); // Sử dụng iterator để xóa phần tử
                            break;
                        }
                    }
                }

                if(rooms.size() < bookingDto.getRooms().get(roomType)){
                    return new Message("Not enough room for room type " + roomType);
                }

                for(int i =0; i<bookingDto.getRooms().get(roomType); i++){
                    Room room = rooms.get(i);
                    booking.getRooms().add(room);
                    room.getBookings().add(booking);
                    List<DateRent> temp = new ArrayList<>();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(bookingDto.getDateCheckIn());
                    while(!calendar.getTime().after(bookingDto.getDateCheckOut())){
                        DateRent dateRent = new DateRent();
                        dateRent.setRoom(room);
                        dateRent.setDate(calendar.getTime());
                        temp.add(dateRent);
                        calendar.add(Calendar.DATE, 1);
                    }
                    dateRents.addAll(temp);

                }
            }
            booking.setDateCheckIn(bookingDto.getDateCheckIn());
            booking.setDateCheckOut(bookingDto.getDateCheckOut());
            booking.setAccount(account);
            booking.setTotalBill(bookingDto.getTotalBill());
            booking.setDescription(bookingDto.getDescription());
            for(DateRent dateRent : dateRents){
                dateRentRepository.save(dateRent);
            }
            bookingRepository.save(booking);
            return new Message("Dat phong thanh cong!");
        } throw new InvalidException("Account is not found for the id " + id);
    }

    @Override
    public List<BookingDto> allBooking(Long accountId) {
        List<Booking> bookings = bookingRepository.findByAccountId(accountId);
        List<BookingDto> bookingDtos = new ArrayList<>();
        for (Booking booking : bookings){
            BookingDto bookingDto = new BookingDto();
            Set<Room> rooms = booking.getRooms();
            HashMap<String , Integer> map = new HashMap<>();
            for(Room room : rooms){
                map.put(room.getRoomType(), map.getOrDefault(room.getRoomType(), 0) +1);
            }
            Room firstRoom = null;
            for(Room room : rooms){
                firstRoom = room;
                break;
            }
            assert firstRoom != null;
            bookingDto.setNameHotel(firstRoom.getHotel().getName());
            bookingDto.setRooms(map);
            bookingDto.setDescription(booking.getDescription());
            bookingDto.setTotalBill(booking.getTotalBill());
            bookingDto.setDateCheckIn(booking.getDateCheckIn());
            bookingDto.setDateCheckOut(booking.getDateCheckOut());
            bookingDtos.add(bookingDto);
        }
        return bookingDtos;

    }

    @Override
    public List<BookingDto> searchByPhoneCustomer(String phone, Long hotelId) throws InvalidException {
        List<Booking> bookings = bookingRepository.findByPhoneAccount(phone);
        Iterator<Booking> iterator = bookings.iterator();
        while(iterator.hasNext()){
            Booking booking = iterator.next();
            if(!roomService.isBookingInHotel(booking.getId(), hotelId)) iterator.remove();
        }
        if(bookings.isEmpty()) throw new InvalidException("Invalid booking");
        else{
            List<BookingDto> bookingDtos = new ArrayList<>();
            for (Booking booking : bookings){
                BookingDto bookingDto = new BookingDto();
                bookingDto.setDateCheckIn(booking.getDateCheckIn());
                bookingDto.setDateCheckOut(booking.getDateCheckOut());
                bookingDto.setTotalBill(booking.getTotalBill());
                bookingDto.setDescription(booking.getDescription());
                bookingDto.setListRoomId(bookingRepository.getBookingRoomIds(booking.getId()));
                bookingDtos.add(bookingDto);
            }
            return bookingDtos;
        }
    }
}
