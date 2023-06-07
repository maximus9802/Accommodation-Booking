package com.quyvx.accommodationbooking.controller;

import com.quyvx.accommodationbooking.dto.BookingDto;
import com.quyvx.accommodationbooking.dto.Message;
import com.quyvx.accommodationbooking.dto.RoomDto;
import com.quyvx.accommodationbooking.exception.InvalidException;
import com.quyvx.accommodationbooking.service.account.AccountService;
import com.quyvx.accommodationbooking.service.booking.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private BookingService bookingService;

    @PostMapping("/{id}/booking/new")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<Message> createBooking(@PathVariable("id") Long accountId,
                                                 @RequestBody BookingDto bookingDto
    ) throws InvalidException {
        return ResponseEntity.ok(bookingService.newBooking(accountId, bookingDto));
    }

    @GetMapping("/{id}/booking/all")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<List<BookingDto>> allBooking(@PathVariable("id") Long accountId) throws InvalidException {
        return ResponseEntity.ok(bookingService.allBooking(accountId));
    }





}
