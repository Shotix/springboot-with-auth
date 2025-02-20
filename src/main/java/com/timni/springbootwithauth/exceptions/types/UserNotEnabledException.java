package com.timni.springbootwithauth.exceptions.types;

import org.springframework.http.HttpStatus;

public class UserNotEnabledException extends RootException {
    public UserNotEnabledException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
    
    public UserNotEnabledException() {
        super(HttpStatus.UNAUTHORIZED, "user.not.enabled");
    }
}
