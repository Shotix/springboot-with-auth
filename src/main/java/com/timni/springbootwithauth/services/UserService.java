package com.timni.springbootwithauth.services;

import com.timni.springbootwithauth.entities.User;
import com.timni.springbootwithauth.exceptions.types.ResourceNotFoundException;
import com.timni.springbootwithauth.repositories.UserRepository;
import com.timni.springbootwithauth.services.base.BaseService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional
@Service
public class UserService extends BaseService<User, String> {
    @Getter
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserRepository getRepository() {
        return userRepository;
    }
    
    public Optional<User> login(final String username) {
        return userRepository.findByUsername(username);
    }
    
    public Optional<User> findByUsername(final String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(final String email) {
        return userRepository.findByEmail(email);
    }
    
    public User getAuthenticatedUser(Authentication authentication) {
        return findByUsername(authentication.getName()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
