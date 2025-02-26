package com.timni.springbootwithauth.services;

import com.timni.springbootwithauth.entities.RefreshToken;
import com.timni.springbootwithauth.repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Creates and stores a new refresh token.
     *
     * @param userId The user identifier.
     * @param token The refresh token string.
     * @param expiryDate The expiration date/time.
     * @return The saved RefreshToken document.
     */
    public RefreshToken createRefreshToken(String userId, String token, LocalDateTime expiryDate) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUserId(userId);
        refreshToken.setCreatedAt(LocalDateTime.now());
        refreshToken.setExpiryDate(expiryDate);
        refreshToken.setRevoked(false);
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public void revokeToken(RefreshToken token) {
        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }
}
