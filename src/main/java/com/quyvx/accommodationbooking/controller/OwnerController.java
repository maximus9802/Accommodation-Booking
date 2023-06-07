package com.quyvx.accommodationbooking.controller;

import com.quyvx.accommodationbooking.dto.HotelDto;
import com.quyvx.accommodationbooking.dto.Message;
import com.quyvx.accommodationbooking.dto.RoomDto;
import com.quyvx.accommodationbooking.exception.InvalidException;
import com.quyvx.accommodationbooking.model.Hotel;
import com.quyvx.accommodationbooking.service.account.AccountService;
import com.quyvx.accommodationbooking.service.hotel.HotelService;
import com.quyvx.accommodationbooking.service.room.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/owner")
public class OwnerController {

    @Autowired
    private HotelService hotelService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private RoomService roomService;

    @PostMapping("/{id}/newHotel")
    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    public ResponseEntity<Message> createHotel(@PathVariable("id") Long id,
                                               @RequestBody @Valid HotelDto hotelDto
    )throws Exception {
        return ResponseEntity.ok(hotelService.save(id, hotelDto));
    }

    @GetMapping("/{id}/allHotel")
    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    public List<HotelDto> getAllHotel(@PathVariable("id") Long id,
                                          @RequestParam(defaultValue = "0") Integer pageNumber,
                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                          @RequestParam(defaultValue = "id") String sortBy
    ){
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        List<Hotel> hotels = hotelService.findByAccountId(id, pageable);
        List<HotelDto> hotelDtos = new ArrayList<>();
        for (Hotel hotel : hotels){
            HotelDto tempHotel = new HotelDto();
            tempHotel.setId(hotel.getId());
            tempHotel.setNameHotel(hotel.getName());
            tempHotel.setLocation(hotel.getLocation());
            tempHotel.setScore(hotel.getScore());
            tempHotel.setShortDescription(hotel.getShortDescription());
            tempHotel.setDetailDescription(hotel.getDetailDescription());
            tempHotel.setAssess(hotel.getAssess());
            tempHotel.setAvatarHotel(hotel.getAvatarHotel());
            tempHotel.setNumberRating(hotel.getNumberRating());
            hotelDtos.add(tempHotel);
        }
        return hotelDtos;
    }

    @PostMapping("/{id}/{idHotel}/new_room")
    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    public ResponseEntity<String> createRoom(@PathVariable("id") Long idAccount,
                             @PathVariable("idHotel") Long idHotel,
                             @RequestBody RoomDto roomDto
    ) throws InvalidException {
        return ResponseEntity.ok(roomService.save(idAccount, idHotel, roomDto));
    }
}
