package com.timni.springbootwithauth.exceptions.types;

import org.springframework.http.HttpStatus;

public class EmailNotUniqueException extends RootException {
    public EmailNotUniqueException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
    
    public EmailNotUniqueException() {
      super(HttpStatus.BAD_REQUEST, "user.register.emailNotUnique");
    }
}
