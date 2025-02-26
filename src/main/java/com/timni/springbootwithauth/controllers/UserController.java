package com.timni.springbootwithauth.controllers;

import com.timni.springbootwithauth.constants.AppUrls;
import com.timni.springbootwithauth.constants.UserConstants;
import com.timni.springbootwithauth.controllers.base.BaseController;
import com.timni.springbootwithauth.entities.User;
import com.timni.springbootwithauth.exceptions.types.EmailNotUniqueException;
import com.timni.springbootwithauth.exceptions.types.UsernameNotUniqueException;
import com.timni.springbootwithauth.mappers.UserMapper;
import com.timni.springbootwithauth.requests.user.*;
import com.timni.springbootwithauth.responses.AuthenticationResponse;
import com.timni.springbootwithauth.responses.UserResponse;
import com.timni.springbootwithauth.responses.base.ApiResponse;
import com.timni.springbootwithauth.services.AuthenticationService;
import com.timni.springbootwithauth.services.UserService;
import com.timni.springbootwithauth.utils.ResponseBuilder;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;

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

    // TODO: Move to AuthenticationController
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(
            @Valid @RequestBody final CreateUserRequest request,
            HttpServletResponse response) {

        log.info("[request] register {}", request);

        // TODO: Move this to a validation via annotation
        if (service.findByUsername(request.username()).isPresent()) {
            throw new UsernameNotUniqueException("user.register.usernameNotUnique");
        }
        if (service.findByEmail(request.email()).isPresent()) {
            throw new EmailNotUniqueException("user.register.emailNotUnique");
        }

        User user = mapper.toEntity(request);
        user.setRoles(UserConstants.DEFAULT_ROLES);
        user.setEnabled(true);
        user.setPassword(authenticationService.hashPassword(user.getPassword()));
        User createdUser = service.create(user);

        // Generate both access and refresh tokens.
        AuthenticationResponse authResponse = authenticationService.authenticate(createdUser);

        addRefreshTokenCookie(response, authResponse.refreshToken());

        // Return only the access token in the JSON response.
        return ResponseBuilder.created("User registered successfully", authResponse.accessToken());
    }

    // TODO: Move to AuthenticationController
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(
            @Valid @RequestBody final LoginRequest request,
            HttpServletResponse response) {

        log.info("[request] login {}", request);

        User user = service.login(request.username())
                .orElseThrow(() -> new UsernameNotFoundException("user.login.credentials.invalid"));

        if (!authenticationService.validatePassword(request.password(), user.getPassword())) {
            throw new UsernameNotFoundException("user.login.credentials.invalid");
        }

        // Generate both tokens.
        AuthenticationResponse authResponse = authenticationService.authenticate(user);

        addRefreshTokenCookie(response, authResponse.refreshToken());

        // Return the access token in the response body.
        return ResponseBuilder.success("Login successful", authResponse.accessToken());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMe() {
        log.info("[request] get authenticated user");

        return ResponseBuilder.success("User retrieved successfully", mapper.toResponse(service.getAuthenticatedUser(SecurityContextHolder.getContext().getAuthentication())));
    }


    public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false); // TODO: SET THIS TO TRUE IN PRODUCTION SO THAT THE COOKIE IS ONLY SENT OVER HTTPS.
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days expiration.
        response.addCookie(refreshCookie);
    }
}
