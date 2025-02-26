package com.timni.springbootwithauth.controllers;

import com.timni.springbootwithauth.constants.AppUrls;
import com.timni.springbootwithauth.entities.RefreshToken;
import com.timni.springbootwithauth.infra.auth.providers.MyUserDetailsService;
import com.timni.springbootwithauth.infra.auth.providers.jwt.JwtUtil;
import com.timni.springbootwithauth.services.RefreshTokenService;
import com.timni.springbootwithauth.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.http.Cookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Getter
@Slf4j
@RestController
@RequestMapping(AuthenticationController.BASE_URL)
@RequiredArgsConstructor
public class AuthenticationController {

    public static final String BASE_URL = AppUrls.BASE_URL + "/auth";

    private final JwtUtil jwtUtil;
    private final MyUserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        // Get refresh token from cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    String refreshTokenStr = cookie.getValue();
                    
                    if (!jwtUtil.validateRefreshToken(refreshTokenStr)) {
                        break;
                    }

                    Optional<?> tokenOpt = refreshTokenService.findByToken(refreshTokenStr);
                    if (tokenOpt.isEmpty()) {
                        break;
                    }

                    var storedToken = (RefreshToken) tokenOpt.get();
                    
                    if (storedToken.isRevoked() || storedToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                        break;
                    }
                    
                    refreshTokenService.revokeToken(storedToken);
                    
                    String username = jwtUtil.extractUsernameFromRefreshToken(refreshTokenStr);
                    // Get the userId by the username or else throw an exception
                    var user = userService.findByUsername(username)
                            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                    
                    var userDetails = userDetailsService.loadUserByUsername(username);
                    
                    String newAccessToken = jwtUtil.generateToken(userDetails);
                    String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);

                    Date newExpiry = jwtUtil.extractExpirationFromRefreshToken(newRefreshToken);
                    LocalDateTime newExpiryDate = newExpiry.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    
                    refreshTokenService.createRefreshToken(user.getId(), newRefreshToken, newExpiryDate);
                    
                    Cookie newCookie = new Cookie("refreshToken", newRefreshToken);
                    newCookie.setHttpOnly(true);
                    newCookie.setSecure(false); // TODO: Set to true in production
                    newCookie.setPath("/");
                    newCookie.setMaxAge(7 * 24 * 60 * 60);
                    response.addCookie(newCookie);

                    return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Refresh token is invalid or missing"));
    }
}
