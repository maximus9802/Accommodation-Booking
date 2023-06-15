package com.quyvx.accommodationbooking.service.jwt;

import com.quyvx.accommodationbooking.model.RefreshToken;
import com.quyvx.accommodationbooking.repository.AccountRepository;
import com.quyvx.accommodationbooking.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private AccountRepository accountRepository;

    public RefreshToken createRefreshToken(String username){
        RefreshToken refreshToken = RefreshToken.builder()
                .account(accountRepository.findByUsername(username).get())
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByRefreshToken(String token){
        return  refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token was expired. Please make a new signin request");
        }
        return  token;
    }
}
