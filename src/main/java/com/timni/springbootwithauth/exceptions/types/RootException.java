package com.timni.springbootwithauth.exceptions.types;

import com.timni.springbootwithauth.responses.base.ApiErrorDetails;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class RootException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 6378336966214073013L;

    private final HttpStatus httpStatus;
    private final List<ApiErrorDetails> errors = new ArrayList<>();

    public RootException(@NotNull final HttpStatus httpStatus) {
        super();
        this.httpStatus = httpStatus;
    }

    public RootException(@NotNull final HttpStatus httpStatus, final String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
