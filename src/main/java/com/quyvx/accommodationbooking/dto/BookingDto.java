package com.quyvx.accommodationbooking.dto;

import lombok.*;

import java.util.Date;
import java.util.HashMap;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class BookingDto {

    private HashMap<String, Integer> rooms = new HashMap<>();

    private Long hotelId;

    private String nameHotel;

    private Date dateCheckIn;

    private Date dateCheckOut;

    private int totalBill;

    private String description;


}
