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
}
