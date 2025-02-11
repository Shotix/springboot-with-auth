package com.timni.springbootwithauth.utils;

import com.timni.springbootwithauth.responses.base.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder<T> {
    public static <T> ResponseEntity<ApiResponse<T>> buildResponse(HttpStatus status, String message, T data) {
        return new ResponseEntity<>(new ApiResponse<>(message, status.value(), data), status);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {
        return buildResponse(HttpStatus.OK, message, data);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {
        return buildResponse(HttpStatus.CREATED, message, data);
    }

    public static <T> ResponseEntity<ApiResponse<T>> noContent(String message) {
        return buildResponse(HttpStatus.NO_CONTENT, message, null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> badRequest(String message) {
        return buildResponse(HttpStatus.BAD_REQUEST, message, null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> unauthorized(String message) {
        return buildResponse(HttpStatus.UNAUTHORIZED, message, null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> forbidden(String message) {
        return buildResponse(HttpStatus.FORBIDDEN, message, null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> notFound(String message) {
        return buildResponse(HttpStatus.NOT_FOUND, message, null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> internalServerError(String message) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, message, null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> notInUse(String message) {
        return buildResponse(HttpStatus.NOT_IMPLEMENTED, message, null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> notImplemented(String message) {
        return buildResponse(HttpStatus.NOT_IMPLEMENTED, message, null);
    }
}
