package com.quyvx.accommodationbooking.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class HotelDetail extends HotelDto{
    private List<RoomDto> rooms = new ArrayList<>();
    private Map<String, Integer> roomNumber = new HashMap<>();
}
