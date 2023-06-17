package com.quyvx.accommodationbooking.service.booking;

import com.quyvx.accommodationbooking.dto.BookingDto;
import com.quyvx.accommodationbooking.dto.Message;
import com.quyvx.accommodationbooking.dto.NotificationDto;
import com.quyvx.accommodationbooking.exception.InvalidException;
import com.quyvx.accommodationbooking.model.*;
import com.quyvx.accommodationbooking.repository.*;
import com.quyvx.accommodationbooking.service.daterent.DateRentService;
import com.quyvx.accommodationbooking.service.room.RoomService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
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
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public NotificationDto newBooking(Long id, BookingDto bookingDto) throws InvalidException {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if(optionalAccount.isPresent()){
            Account account = optionalAccount.get();
            Booking booking = new Booking();
            List<DateRent> dateRents = new ArrayList<>();
            for(String roomType : bookingDto.getRooms().keySet()){
                List<Room> rooms = roomRepository.findByHotelIdAndRoomType(bookingDto.getHotelId(), roomType);
                if(rooms.isEmpty()){
                    return NotificationDto
                            .builder()
                            .message("The hotel does not have rooms of this type. Retry!")
                            .build();
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
                    return NotificationDto
                            .builder()
                            .message("The hotel does not have enough rooms of this type. Retry!")
                            .build();
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
            Booking temp = bookingRepository.save(booking);
            var noti = Notification
                    .builder()
                    .booking(temp)
                    .message("Successful booking!")
                    .account(account)
                    .build();
            notificationRepository.save(noti);
            return NotificationDto
                    .builder()
                    .accountId(account.getId())
                    .bookingId(temp.getId())
                    .message("Successful booking!")
                    .build();
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
            bookingDto.setStatus(booking.getStatus());
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
                bookingDto.setStatus(booking.getStatus());
                bookingDto.setDescription(booking.getDescription());
                bookingDto.setListRoomId(bookingRepository.getBookingRoomIds(booking.getId()));
                bookingDtos.add(bookingDto);
            }
            return bookingDtos;
        }
    }

    @Override
    public NotificationDto changeStatus(Long idAccount, Long idHotel, Long idBooking, String status) throws Exception {
        Optional<Account> optionalAccount = accountRepository.findById(idAccount);
        if (optionalAccount.isPresent()){
            Account account = optionalAccount.get();
            Optional<Booking> optionalBooking = bookingRepository.findById(idBooking);
            if(optionalBooking.isPresent()){
                Booking booking = optionalBooking.get();
                Set<Room> rooms = booking.getRooms();
                Hotel hotel = new Hotel();
                for(Room room : rooms){
                    hotel = room.getHotel();
                    break;
                }
                if(hotel.getId() == idHotel){
                    booking.setStatus(status);
                    bookingRepository.save(booking);

                    var noti = Notification
                            .builder()
                            .message("Update status booking" + idBooking + "successful!")
                            .account(account)
                            .booking(booking)
                            .build();
                    return NotificationDto
                            .builder()
                            .message("Update status booking" + idBooking + "successful!")
                            .accountId(idAccount)
                            .bookingId(idBooking)
                            .build();
                } throw new Exception("This is nt your rating!");
            } throw  new Exception("Invalid booking!");
        } throw new Exception("Account not found.");
    }

    @Override
    public NotificationDto cancelBookingByCustomer(Long idAccount, Long idBooking, String reason) throws Exception {
        Optional<Account> optionalAccount = accountRepository.findById(idAccount);
        if(optionalAccount.isPresent()){
            Account account = optionalAccount.get();
            Optional<Booking> optionalBooking = bookingRepository.findById(idBooking);
            if(optionalBooking.isPresent()){
                Booking booking = optionalBooking.get();
                if(booking.getAccount().getId() == idAccount){
                    if(checkDateBefore(booking.getDateCheckIn())){
                        booking.setStatus("canceled");

                        account.setScore((account.getScore()-10));

                        var notiCustomer = Notification
                                .builder()
                                .account(account)
                                .booking(booking)
                                .message("Cancel booking " + booking.getId() + " successful!")
                                .build();
                        notificationRepository.save(notiCustomer);

                        Set<Room> rooms = booking.getRooms();
                        for (Room room: rooms){
                            Hotel hotel = room.getHotel();
                            Account accountOwner = room.getHotel().getAccount();

                            var notiOwner = Notification
                                    .builder()
                                    .account(accountOwner)
                                    .hotel(hotel)
                                    .message("Customer canceled booking #" + booking.getId() + " .Reason: " + reason)
                                    .build();
                            notificationRepository.save(notiOwner);
                        }

                        for(Room room : rooms){
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(booking.getDateCheckIn());
                            while(!calendar.getTime().after(booking.getDateCheckOut())){
                                Date date = calendar.getTime();
                                dateRentRepository.deleteByRoomIdAndDate(room.getId(), date);
                                calendar.add(Calendar.DATE, 1);
                            }
                        }

                        bookingRepository.save(booking);
                        accountRepository.save(account);

                        return NotificationDto
                                .builder()
                                .message("Cancel booking #" + booking.getId() + " successful!")
                                .bookingId(booking.getId())
                                .accountId(account.getId())
                                .build();
                    } throw new Exception("You cannot cancel booking #" + booking.getId() +" .Because: the time allowed to cancel has passed." );
                } throw new Exception("This is not your booking!");
            } throw new InvalidException("Invalid Booking");
        } throw new InvalidException("Invalid Account");
    }

    @Override
    public NotificationDto cancelBookingByOwner(Long idAccount, Long idHotel, Long idBooking, String reason) throws Exception {
        Optional<Account> accountOptional = accountRepository.findById(idAccount);
        if(accountOptional.isPresent()){
            Account account = accountOptional.get();
            Optional<Hotel> hotelOptional = hotelRepository.findById(idHotel);
            if(hotelOptional.isPresent()){
                Hotel hotel = hotelOptional.get();
                if(hotel.getAccount().getId() == idAccount){
                    Optional<Booking> bookingOptional = bookingRepository.findById(idBooking);
                    if(bookingOptional.isPresent()){
                        Booking booking = bookingOptional.get();
                        if(checkDateAfter(booking.getDateCheckIn())){
                            booking.setStatus("no check-in");
                            bookingRepository.save(booking);

                            Set<Room> rooms = booking.getRooms();
                            for(Room room : rooms){
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(booking.getDateCheckIn());
                                while(!calendar.getTime().after(booking.getDateCheckOut())){
                                    Date date = calendar.getTime();
                                    dateRentRepository.deleteByRoomIdAndDate(room.getId(), date);
                                    calendar.add(Calendar.DATE, 1);
                                }
                            }

                            Account accountCustomer = booking.getAccount();
                            accountCustomer.setScore(accountCustomer.getScore()-50);
                            accountRepository.save(accountCustomer);

                            var notiOwner = Notification
                                    .builder()
                                    .message("You cancel booking #"+ booking.getId() + " successful!")
                                    .account(account)
                                    .booking(booking)
                                    .build();
                            notificationRepository.save(notiOwner);

                            var notiCustomer = Notification
                                    .builder()
                                    .message("Your booking #"+ booking.getId() + " has been canceled by the hotel owner. Because: " + reason)
                                    .account(accountCustomer)
                                    .booking(booking)
                                    .build();
                            notificationRepository.save(notiCustomer);

                            return  NotificationDto
                                    .builder()
                                    .message("You cancel booking #"+ booking.getId() + " successful!")
                                    .accountId(account.getId())
                                    .bookingId(booking.getId())
                                    .build();

                        } throw new Exception("You cannot change booking " + booking.getId() + " status right now!");
                    } throw new InvalidException("Invalid Booking");
                } throw new Exception("This is not your hotel");
            }throw new InvalidException("Invalid Hotel");
        }throw new InvalidException("Invalid Account!");
    }

    @Override
    public boolean checkDateBefore(Date date) {
        LocalDate currentDate = LocalDate.now();

        LocalDate dateCheck = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if(currentDate.isBefore(dateCheck.minusDays(3))) return true;
        else return false;
    }

    @Override
    public boolean checkDateAfter(Date date) {
        LocalDate currentDate = LocalDate.now();

        LocalDate dateCheck = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if(currentDate.isAfter(dateCheck.plusDays(1))) return  true;
        else return false;
    }

}
