package com.quyvx.accommodationbooking.controller;

import com.quyvx.accommodationbooking.dto.AccountDto;
import com.quyvx.accommodationbooking.dto.Message;
import com.quyvx.accommodationbooking.dto.NotificationDto;
import com.quyvx.accommodationbooking.dto.PassUser;
import com.quyvx.accommodationbooking.exception.InvalidException;
import com.quyvx.accommodationbooking.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.cache.SpringCacheBasedUserCache;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
@CrossOrigin(origins = "http://localhost:3000")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PutMapping("/{id}/update")
    @PreAuthorize("hasAuthority('ROLE_OWNER', 'ROLE_CUSTOMER')")
    public ResponseEntity<NotificationDto> updateInfo(@PathVariable("id") Long id,
                                                      @RequestBody AccountDto accountDto
    ) throws InvalidException {
        return ResponseEntity.ok(accountService.updateAccount(id, accountDto));
    }

    @PutMapping("/{id}/changePassword")
    @PreAuthorize("hasAuthority('ROLE_OWNER', 'ROLE_CUSTOMER')")
    public ResponseEntity<NotificationDto> changePassword(@PathVariable("id") Long id,
                                                  @RequestBody PassUser passUser
    ) throws InvalidException {
        return ResponseEntity.ok(accountService.changePassword(id, passUser));
    }

    @PutMapping("/{id}/changeUsername")
    @PreAuthorize("hasAuthority('ROLE_OWNER', 'ROLE_CUSTOMER')")
    public ResponseEntity<NotificationDto> changeUsername(@PathVariable("id") Long id,
                                                  @RequestBody PassUser passUser
    ) throws InvalidException {
        return ResponseEntity.ok(accountService.changeUsername(id, passUser));
    }

    @GetMapping("/{idAccount}/info")
    @PreAuthorize("hasAuthority('ROLE_OWNER', 'ROLE_CUSTOMER)")
    public ResponseEntity<AccountDto> getInformation(@PathVariable("idAccount") Long id) throws InvalidException{
        return ResponseEntity.ok(accountService.getInformation(id));
    }
}
