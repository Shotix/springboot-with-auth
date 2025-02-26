package com.timni.springbootwithauth.services;

import com.timni.springbootwithauth.repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenCleanupService {

    private final RefreshTokenRepository refreshTokenRepository;

    // Run every hour (3600000 milliseconds)
    @Scheduled(fixedRate = 3600000)
    public void cleanupRevokedTokens() {
        refreshTokenRepository.deleteByRevokedTrueAndExpiryDateBefore(LocalDateTime.now());
        
        // Option 2: Delete all revoked tokens
        refreshTokenRepository.deleteByRevokedTrue();
    }
}
