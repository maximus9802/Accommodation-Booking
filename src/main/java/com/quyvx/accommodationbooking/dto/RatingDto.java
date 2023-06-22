package com.quyvx.accommodationbooking.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingDto {
    private String comment;
    private int score;
    private String roomType;
    private String nameHotel;
}
