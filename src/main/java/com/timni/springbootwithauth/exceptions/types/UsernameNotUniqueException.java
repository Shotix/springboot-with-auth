package com.timni.springbootwithauth.exceptions.types;

import org.springframework.http.HttpStatus;

public class UsernameNotUniqueException extends RootException {
    public UsernameNotUniqueException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
    
    public UsernameNotUniqueException() {
        super(HttpStatus.BAD_REQUEST, "user.register.usernameNotUnique");
    }
}
