package com.quyvx.accommodationbooking.controller;

import com.quyvx.accommodationbooking.dto.BookingDto;
import com.quyvx.accommodationbooking.dto.Message;
import com.quyvx.accommodationbooking.dto.RatingDto;
import com.quyvx.accommodationbooking.dto.RoomDto;
import com.quyvx.accommodationbooking.exception.InvalidException;
import com.quyvx.accommodationbooking.service.account.AccountService;
import com.quyvx.accommodationbooking.service.booking.BookingService;
import com.quyvx.accommodationbooking.service.rating.RatingService;
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
    @Autowired
    private RatingService ratingService;

    @PostMapping("/{idAccount}/booking/new")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<Message> createBooking(@PathVariable("idAccount") Long accountId,
                                                 @RequestBody BookingDto bookingDto
    ) throws InvalidException {
        return ResponseEntity.ok(bookingService.newBooking(accountId, bookingDto));
    }

    @GetMapping("/{idAccount}/booking/all")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<List<BookingDto>> allBooking(@PathVariable("idAccount") Long accountId) throws InvalidException {
        return ResponseEntity.ok(bookingService.allBooking(accountId));
    }

    @PostMapping("/{idAccount}/{idBooking}/{roomId}/newRating")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<Message> newRating(@PathVariable("idAccount") Long idAccount,
                                             @PathVariable("idBooking") Long idBooking,
                                             @PathVariable("roomId") Long roomId,
                                             @RequestBody RatingDto ratingDto
    ) throws Exception {
        return ResponseEntity.ok(ratingService.newRating(idAccount, roomId, idBooking, ratingDto));
    }

    @DeleteMapping("/{idAccount}/{idBooking}/deleteRating")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<Message> deleteRating(@PathVariable("idAccount") Long idAccount,
                                                @PathVariable("idBooking") Long idBooking,
                                                @RequestParam("idRating") Long idRating
    ) throws Exception {
        return ResponseEntity.ok(ratingService.deleteRating(idAccount, idBooking, idRating));
    }


}
