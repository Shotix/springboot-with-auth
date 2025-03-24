package com.timni.springbootwithauth.responses;

import com.timni.springbootwithauth.entities.UserSettings;

public record UserResponse(
        String username,
        UserSettings userSettings
) {
}
