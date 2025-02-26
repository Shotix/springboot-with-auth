package com.timni.springbootwithauth.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "refresh_tokens")
public class RefreshToken {
    @Id
    private String id;
    private String token;           
    private String userId;          
    private boolean revoked;       
    @CreatedDate
    private LocalDateTime createdAt;
    @Indexed(expireAfter = "0s")
    private LocalDateTime expiryDate; 
}
