package com.quyvx.accommodationbooking.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class HotelDto {
    private String nameHotel;
    private String location;
    private String shortDescription;
    private String detailDescription;
    private int assess;
    private String avatarHotel;
}
