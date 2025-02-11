package com.timni.springbootwithauth.exceptions.types;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(code = NOT_FOUND)
public class ResourceNotFoundException extends RootException {

    @Serial
    private static final long serialVersionUID = 26377136569699646L;

    public ResourceNotFoundException() {
        super(NOT_FOUND, "entity not found, please provide a valid id");
    }

    public ResourceNotFoundException(final Long id) {
        super(NOT_FOUND, format("entity with id '%s' not found", id));
    }

    public ResourceNotFoundException(final Object id) {
        super(NOT_FOUND, format("entity with id '%s' not found", id));
    }

    public ResourceNotFoundException(final String message) {
        super(NOT_FOUND, message);
    }
}
