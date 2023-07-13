package com.quyvx.accommodationbooking.controller;

import com.quyvx.accommodationbooking.dto.*;
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

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/{idAccount}/booking/new")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<NotificationDto> createBooking(@PathVariable("idAccount") Long accountId,
                                                         @RequestBody BookingDto bookingDto
    ) throws Exception {
        return ResponseEntity.ok(bookingService.newBooking(accountId, bookingDto));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{idAccount}/booking")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<BookingDto> searchBookingById(@PathVariable("idAccount") Long idAccount,
                                                        @RequestParam Long bookingId
    ) throws Exception {
        return ResponseEntity.ok(bookingService.searchBookingById(idAccount, bookingId));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{idAccount}/booking/all")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<List<BookingDto>> allBooking(@PathVariable("idAccount") Long accountId) throws InvalidException {
        return ResponseEntity.ok(bookingService.allBooking(accountId));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/{idAccount}/{idBooking}/newRating")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<NotificationDto> newRating(@PathVariable("idAccount") Long idAccount,
                                             @PathVariable("idBooking") Long idBooking,
                                             @RequestBody RatingDto ratingDto
    ) throws Exception {
        return ResponseEntity.ok(ratingService.newRating(idAccount, idBooking, ratingDto));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/{idAccount}/{idBooking}/deleteRating")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<NotificationDto> deleteRating(@PathVariable("idAccount") Long idAccount,
                                                @PathVariable("idBooking") Long idBooking,
                                                @RequestParam("idRating") Long idRating
    ) throws Exception {
        return ResponseEntity.ok(ratingService.deleteRating(idAccount, idBooking, idRating));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/{accountId}/{idBooking}/updateRating")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<NotificationDto> updateRating(@PathVariable("accountId") Long accountId,
                                                        @PathVariable("idBooking") Long idBooking,
                                                        @RequestParam("idRating") Long idRating,
                                                        @RequestBody RatingDto ratingDto
    ) throws Exception {
        return ResponseEntity.ok(ratingService.updateRating(accountId, idBooking, idRating, ratingDto));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{accountId}/rating/all")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<List<RatingDto>> getAllRatingByAccountId(@PathVariable("accountId") Long accountId) throws Exception {
        return ResponseEntity.ok(ratingService.getAllRatingByAccountId(accountId));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/{accountId}/{idBooking}/cancelBooking")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<NotificationDto> cancelBookingByCustomer(@PathVariable("accountId") Long accountId,
                                                                   @PathVariable("idBooking") Long idBooking,
                                                                   @RequestParam String reason
    ) throws Exception {
        return ResponseEntity.ok(bookingService.cancelBookingByCustomer(accountId, idBooking, reason));
    }

}
