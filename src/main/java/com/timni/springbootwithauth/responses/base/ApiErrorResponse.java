package com.timni.springbootwithauth.responses.base;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ApiErrorResponse {
    private int statusCode;
    private String message;
    private String instance;
    private List<ApiErrorDetails> errors;
}
