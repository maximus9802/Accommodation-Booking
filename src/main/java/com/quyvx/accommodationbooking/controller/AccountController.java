package com.quyvx.accommodationbooking.controller;

import com.quyvx.accommodationbooking.dto.AccountDto;
import com.quyvx.accommodationbooking.dto.Message;
import com.quyvx.accommodationbooking.dto.NotificationDto;
import com.quyvx.accommodationbooking.dto.PassUser;
import com.quyvx.accommodationbooking.exception.InvalidException;
import com.quyvx.accommodationbooking.service.account.AccountService;
import com.quyvx.accommodationbooking.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.cache.SpringCacheBasedUserCache;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private NotificationService notificationService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/{id}/update")
    @PreAuthorize("hasAuthority('ROLE_OWNER', 'ROLE_CUSTOMER')")
    public ResponseEntity<NotificationDto> updateInfo(@PathVariable("id") Long id,
                                                      @RequestBody AccountDto accountDto
    ) throws InvalidException {
        return ResponseEntity.ok(accountService.updateAccount(id, accountDto));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/{id}/changePassword")
    @PreAuthorize("hasAuthority('ROLE_OWNER', 'ROLE_CUSTOMER')")
    public ResponseEntity<NotificationDto> changePassword(@PathVariable("id") Long id,
                                                  @RequestBody PassUser passUser
    ) throws InvalidException {
        return ResponseEntity.ok(accountService.changePassword(id, passUser));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/{id}/changeUsername")
    @PreAuthorize("hasAuthority('ROLE_OWNER', 'ROLE_CUSTOMER')")
    public ResponseEntity<NotificationDto> changeUsername(@PathVariable("id") Long id,
                                                  @RequestBody PassUser passUser
    ) throws InvalidException {
        return ResponseEntity.ok(accountService.changeUsername(id, passUser));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{idAccount}/info")
    @PreAuthorize("hasAuthority('ROLE_OWNER', 'ROLE_CUSTOMER)")
    public ResponseEntity<AccountDto> getInformation(@PathVariable("idAccount") Long id) throws InvalidException{
        return ResponseEntity.ok(accountService.getInformation(id));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{idAccount}/noti")
    @PreAuthorize("hasAuthority('ROLE_OWNER', 'ROLE_CUSTOMER')")
    public ResponseEntity<List<NotificationDto>> getNotification(@PathVariable("idAccount") Long idAccount) throws Exception{
        return  ResponseEntity.ok(notificationService.findByAccountId(idAccount));
    }
}
