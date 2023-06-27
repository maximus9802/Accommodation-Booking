package com.quyvx.accommodationbooking.service.notification;

import com.quyvx.accommodationbooking.dto.NotificationDto;
import com.quyvx.accommodationbooking.exception.InvalidException;
import com.quyvx.accommodationbooking.model.Account;
import com.quyvx.accommodationbooking.model.Notification;
import com.quyvx.accommodationbooking.model.Role;
import com.quyvx.accommodationbooking.repository.AccountRepository;
import com.quyvx.accommodationbooking.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService{
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Override
    public NotificationDto sendNoti(Long idAccount, Long idUser, String message) throws Exception {
        Optional<Account> account = accountRepository.findById(idAccount);
        Optional<Account> accountOptional = accountRepository.findById(idUser);
        if(account.isPresent() && accountOptional.isPresent()){
            if(account.get().getRole() == Role.ADMIN){
                var temp = Notification
                        .builder()
                        .message(message)
                        .account(accountOptional.get())
                        .build();
                notificationRepository.save(temp);
                return NotificationDto
                        .builder()
                        .message("Send message success to user #"+idUser + " !")
                        .build();
            } throw new Exception("You are not granted this permission.");
        }throw new InvalidException("Invalid account!");
    }

    @Override
    public List<NotificationDto> findByAccountId(Long idAccount) throws Exception {
        List<Notification> notifications = notificationRepository.findByAccountId(idAccount);
        List<NotificationDto> list = new ArrayList<>();
        for(Notification notification : notifications){
            NotificationDto notificationDto = new NotificationDto();
            notificationDto.setId(notification.getId());
            notificationDto.setAccountId(notification.getAccount().getId());
            if(notification.getHotel() != null){
                notificationDto.setBookingId(notification.getBooking().getId());
            }
            if(notification.getBooking() != null){
                notificationDto.setBookingId(notification.getBooking().getId());
            }
            if(notification.getHotel() != null){
                notificationDto.setHotelId(notification.getHotel().getId());
            }
            if(notification.getRating() != null){
                notificationDto.setRatingId(notification.getRating().getId());
            }
            notificationDto.setMessage(notification.getMessage());
            list.add(notificationDto);
        }
        return list;
    }
}
