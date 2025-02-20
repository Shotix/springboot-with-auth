package com.timni.springbootwithauth.requests.user;

public record CreateUserRequest(
        String username,
        String email,
        String password
) {
}
