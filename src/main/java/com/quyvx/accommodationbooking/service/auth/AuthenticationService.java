package com.quyvx.accommodationbooking.service.auth;

import com.quyvx.accommodationbooking.dto.auth.AuthenticationRequest;
import com.quyvx.accommodationbooking.dto.auth.AuthenticationResponse;
import com.quyvx.accommodationbooking.dto.auth.RegisterRequest;
import com.quyvx.accommodationbooking.exception.InvalidException;
import com.quyvx.accommodationbooking.exception.UsernameAlreadyExistsException;
import com.quyvx.accommodationbooking.model.Account;
import com.quyvx.accommodationbooking.model.RefreshToken;
import com.quyvx.accommodationbooking.repository.AccountRepository;
import com.quyvx.accommodationbooking.service.account.AccountService;
import com.quyvx.accommodationbooking.service.jwt.JwtService;
import com.quyvx.accommodationbooking.service.jwt.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AccountService accountService;
    private final RefreshTokenService refreshTokenService;
    public AuthenticationResponse register(RegisterRequest request) throws Exception {
        if(accountService.existsUsername(request.getUsername())){
            throw new UsernameAlreadyExistsException("Username already exists!");
        }

        var user = Account.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .name(request.getName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .build();
        accountRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws InvalidException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            var user = accountRepository.findByUsername(request.getUsername())
                    .orElseThrow();
            var jwtToken = jwtService.generateToken(user);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(request.getUsername());
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .refreshToken(refreshToken.getToken())
                    .accountId(user.getId())
                    .build();
        } catch (Exception ex){
            throw  new InvalidException("Invalid username or password!");
        }
    }
}
