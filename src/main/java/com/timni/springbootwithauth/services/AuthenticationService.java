package com.timni.springbootwithauth.services;

import com.timni.springbootwithauth.entities.User;
import com.timni.springbootwithauth.infra.auth.providers.MyUserDetailsService;
import com.timni.springbootwithauth.infra.auth.providers.jwt.JwtUtil;
import com.timni.springbootwithauth.responses.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final MyUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    
    public AuthenticationResponse authenticate(User user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            
            var userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            String accessToken = jwtUtil.generateToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);
            return new AuthenticationResponse(accessToken, refreshToken);
        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}", user.getUsername());
            throw new BadCredentialsException("user.login.credentials.invalid");
        }
    }
    
    public boolean validatePassword(String inputPassword, String storedPassword) {
        return passwordEncoder.matches(inputPassword, storedPassword);
    }
    
    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
