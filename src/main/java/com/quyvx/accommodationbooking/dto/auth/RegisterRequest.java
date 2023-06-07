package com.quyvx.accommodationbooking.dto.auth;

import com.quyvx.accommodationbooking.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;
    private Role role;
    private String name;
    private String address;
    private String phone;
}
