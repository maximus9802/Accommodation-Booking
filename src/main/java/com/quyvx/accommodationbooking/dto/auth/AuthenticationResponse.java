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
public class AuthenticationResponse {
    private String token;
    private String refreshToken;
    private Long accountId;
    private Role role;
}
