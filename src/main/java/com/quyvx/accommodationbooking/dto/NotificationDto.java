package com.quyvx.accommodationbooking.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class NotificationDto {
    private Long id;
    private String message;
    private Long accountId;
    private Long bookingId;
    private Long ratingId;
    private Long hotelId;
}
