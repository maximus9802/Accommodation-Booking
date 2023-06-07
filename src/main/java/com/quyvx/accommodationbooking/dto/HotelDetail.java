package com.quyvx.accommodationbooking.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class HotelDetail extends HotelDto{
    private List<RoomDto> rooms = new ArrayList<>();
}
