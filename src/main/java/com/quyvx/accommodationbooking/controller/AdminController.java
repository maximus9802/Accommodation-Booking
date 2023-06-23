package com.quyvx.accommodationbooking.controller;

import com.quyvx.accommodationbooking.dto.AccountDto;
import com.quyvx.accommodationbooking.dto.NotificationDto;
import com.quyvx.accommodationbooking.service.account.AccountService;
import com.quyvx.accommodationbooking.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{idAccount}/allAccount")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<AccountDto>> getAllAccount(@PathVariable("idAccount") Long  idAccount,
                                                          @RequestParam(defaultValue = "0") Integer pageNumber,
                                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                                          @RequestParam(defaultValue = "score") String sortBy
    ) throws Exception{
        return ResponseEntity.ok(accountService.getAllAccount(idAccount, pageNumber, pageSize ,sortBy));
    }

    @PostMapping("/{idAccount}/sendNoti")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<NotificationDto> sendNotification(@PathVariable("idAccount") Long idAccount,
                                                            @RequestParam Long idUser,
                                                            @RequestParam String message
    ) throws Exception {
        return ResponseEntity.ok(notificationService.sendNoti(idAccount, idUser, message));
    }
}
