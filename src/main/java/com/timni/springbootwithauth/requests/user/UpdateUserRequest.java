package com.timni.springbootwithauth.requests.user;

public record UpdateUserRequest(
        String username,
        String email,
        String password
) {
}
