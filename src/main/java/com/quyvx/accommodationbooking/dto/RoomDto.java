package com.quyvx.accommodationbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {
    private String roomType;
    private int price;
    private String description;
    private List<String> service;
    private int numberRooms;
    private List<String> images;
}
