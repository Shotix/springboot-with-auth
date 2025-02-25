package com.timni.springbootwithauth.responses;

public record AuthenticationResponse(
        String accessToken,
        String refreshToken 
) {
}
