package com.timni.springbootwithauth.infra.auth.providers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.timni.springbootwithauth.utils.ResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(ResponseBuilder.forbidden(getResponseString(accessDeniedException))));
    }

    private static String getResponseString(AccessDeniedException accessDeniedException) {

        return switch (accessDeniedException) {
            case InvalidCsrfTokenException _ -> "Invalid CSRF token";
            default -> "Access Denied";
        };
    }
}

