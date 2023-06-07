package com.quyvx.accommodationbooking.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PassUser {
    private String currentUsername;
    private String newUsername;
    private String currentPassword;
    private String newPassword;
}
