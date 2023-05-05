package com.quyvx.accommodationbooking.dto;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class BookingDto {
    private Date dateCheckIn;

    private Date dateCheckOut;

    private int totalBill;

    private String description;


}
