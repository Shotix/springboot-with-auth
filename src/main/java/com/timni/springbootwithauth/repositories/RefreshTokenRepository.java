package com.timni.springbootwithauth.repositories;

import com.timni.springbootwithauth.entities.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
    Optional<RefreshToken> findByToken(String token);
    
    void deleteByRevokedTrueAndExpiryDateBefore(LocalDateTime time);
    
    void deleteByRevokedTrue();
}
