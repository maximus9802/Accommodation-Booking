package com.quyvx.accommodationbooking.dto;

import com.quyvx.accommodationbooking.model.Role;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDto {
    private String name;
    private String address;
    private String phone;
}
