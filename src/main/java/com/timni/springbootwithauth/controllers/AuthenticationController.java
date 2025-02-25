package com.timni.springbootwithauth.controllers;

import com.timni.springbootwithauth.constants.AppUrls;
import com.timni.springbootwithauth.infra.auth.providers.MyUserDetailsService;
import com.timni.springbootwithauth.infra.auth.providers.jwt.JwtUtil;
import com.timni.springbootwithauth.responses.base.ApiResponse;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.http.Cookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@Getter
@Slf4j
@RestController
@RequestMapping(AuthenticationController.BASE_URL)
@RequiredArgsConstructor
public class AuthenticationController {

    public static final String BASE_URL = AppUrls.BASE_URL + "/auth";

    private final JwtUtil jwtUtil;
    private final MyUserDetailsService userDetailsService;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        // Get refresh token from cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    String refreshToken = cookie.getValue();
                    if (jwtUtil.validateRefreshToken(refreshToken)) {
                        // Extract username from refresh token
                        String username = Jwts.parserBuilder()
                                .setSigningKey(jwtUtil.getRefreshTokenSecret())
                                .build()
                                .parseClaimsJws(refreshToken)
                                .getBody()
                                .getSubject();
                        var userDetails = userDetailsService.loadUserByUsername(username);
                        String newAccessToken = jwtUtil.generateToken(userDetails);
                        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Refresh token is invalid or missing"));
    }
}
