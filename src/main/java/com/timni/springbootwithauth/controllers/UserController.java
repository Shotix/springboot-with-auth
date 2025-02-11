package com.timni.springbootwithauth.controllers;

import com.timni.springbootwithauth.constants.AppUrls;
import com.timni.springbootwithauth.constants.UserConstants;
import com.timni.springbootwithauth.controllers.base.BaseController;
import com.timni.springbootwithauth.entities.User;
import com.timni.springbootwithauth.mappers.UserMapper;
import com.timni.springbootwithauth.requests.user.*;
import com.timni.springbootwithauth.responses.UserResponse;
import com.timni.springbootwithauth.responses.base.ApiResponse;
import com.timni.springbootwithauth.services.AuthenticationService;
import com.timni.springbootwithauth.services.UserService;
import com.timni.springbootwithauth.utils.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@Getter
@Slf4j
@RestController
@RequestMapping(UserController.BASE_URL)
@RequiredArgsConstructor
public class UserController extends BaseController<
        User,
        String,
        CreateUserRequest,
        UpdateUserRequest,
        PatchUserRequest,
        UserResponse> {
    public static final String BASE_URL = AppUrls.BASE_URL + "/users";

    private final UserService service;
    private final UserMapper mapper;
    private final AuthenticationService authenticationService;
    
    
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(
            @Valid @RequestBody final CreateUserRequest request) {
        log.info("[request] register {}", request);
        
        User user = mapper.toEntity(request);
        user.setRoles(UserConstants.defaultRole);
        user.setPassword(authenticationService.hashPassword(user.getPassword()));
        User createdUser = service.create(user);
        
        String token = authenticationService.authenticate(createdUser);
        return ResponseBuilder.created("User registered successfully", token);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login (
            @Valid @RequestBody final LoginRequest request) {

        log.info("[request] login {}", request);
        
        User user = service.login(request.username()).orElseThrow(() -> new UsernameNotFoundException("Credentials Invalid"));
        
        if (!authenticationService.validatePassword(request.password(), user.getPassword())) {
            throw new UsernameNotFoundException("Credentials invalid");
        }
        
        String token = authenticationService.authenticate(user);
        return ResponseBuilder.success("Login successful", token);
    }
}
