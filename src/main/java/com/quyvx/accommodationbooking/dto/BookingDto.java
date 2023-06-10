package com.quyvx.accommodationbooking.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

    private List<Long> listRoomId = new ArrayList<>();


}
