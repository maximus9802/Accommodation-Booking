package com.quyvx.accommodationbooking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class HotelDto {
    private Long id;
    @NotNull(message = "Name Hotel should not be null")
    private String nameHotel;
    @NotNull(message = "Location should not be null")
    private String location;
    private float score;
    private String shortDescription;
    private String detailDescription;
    private int assess;
    private String avatarHotel;
    private int numberRating;
}
