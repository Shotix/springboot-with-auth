package com.timni.springbootwithauth.requests.user;

public record PatchUserRequest(
        String username,
        String email,
        String password
) {
}
