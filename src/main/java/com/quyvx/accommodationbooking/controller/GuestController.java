package com.quyvx.accommodationbooking.controller;

import com.quyvx.accommodationbooking.dto.HotelDetail;
import com.quyvx.accommodationbooking.dto.HotelDto;
import com.quyvx.accommodationbooking.dto.RoomDto;
import com.quyvx.accommodationbooking.dto.auth.AuthenticationRequest;
import com.quyvx.accommodationbooking.dto.auth.AuthenticationResponse;
import com.quyvx.accommodationbooking.dto.auth.RefreshTokenRequest;
import com.quyvx.accommodationbooking.dto.auth.RegisterRequest;
import com.quyvx.accommodationbooking.exception.InvalidException;
import com.quyvx.accommodationbooking.model.Hotel;
import com.quyvx.accommodationbooking.model.RefreshToken;
import com.quyvx.accommodationbooking.service.auth.AuthenticationService;
import com.quyvx.accommodationbooking.service.hotel.HotelService;
import com.quyvx.accommodationbooking.service.jwt.JwtService;
import com.quyvx.accommodationbooking.service.jwt.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/guest")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class GuestController {

    @Autowired
    private HotelService hotelService;
    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) throws Exception {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) throws InvalidException {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping("/hotel")
    public ResponseEntity<List<HotelDto>> searchHotelByLocation(@RequestParam(defaultValue = "VietNam") String location,
                                                          @RequestParam(defaultValue = "0") Integer pageNumber,
                                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                                          @RequestParam(defaultValue = "assess") String sortBy
    ) {
        return ResponseEntity.ok(hotelService.searchByLocation(pageNumber, pageSize, location, sortBy));
    }

    @GetMapping("/hotels")
    public List<HotelDto> allHotel(@RequestParam(defaultValue = "0") Integer pageNumber,
                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                   @RequestParam(defaultValue = "assess") String sortBy

    ){
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        Page<Hotel> hotels = hotelService.findAll(pageable);
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

    @GetMapping("/hotels/{idHotel}/detail")
    public ResponseEntity<HotelDetail> viewHotelDetail(
            @PathVariable("idHotel") Long idHotel
    ) throws InvalidException {
        return ResponseEntity.ok(hotelService.viewHotelDetail(idHotel));
    }

    @PostMapping("/refreshToken")
    public AuthenticationResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return refreshTokenService.findByRefreshToken(refreshTokenRequest.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getAccount)
                .map(account -> {
                    String token = jwtService.generateToken(account);
                    return AuthenticationResponse.builder()
                            .token(token)
                            .refreshToken(refreshTokenRequest.getRefreshToken())
                            .accountId(account.getId())
                            .build();
                }).orElseThrow(()-> new RuntimeException(
                        "Refresh token is not database!"));
    }
}
