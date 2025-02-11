package com.timni.springbootwithauth.exceptions;

import com.timni.springbootwithauth.responses.base.ApiErrorDetails;
import com.timni.springbootwithauth.responses.base.ApiErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import static com.timni.springbootwithauth.constants.AppConstants.API_DEFAULT_ERROR_MESSAGE;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull final MethodArgumentNotValidException ex,
            @NonNull final HttpHeaders headers,
            @NonNull final HttpStatusCode status,
            @NonNull final WebRequest request) {
        log.info(ex.getMessage(), ex);

        final List<ApiErrorDetails> errors = new ArrayList<>();
        for (final ObjectError err : ex.getBindingResult().getAllErrors()) {
            errors.add(
                    ApiErrorDetails.builder()
                            .pointer(((FieldError) err).getField())
                            .reason(err.getDefaultMessage())
                            .build());
        }

        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .statusCode(BAD_REQUEST.value())
                .message("Validation failed.")
                .instance(request.getDescription(false))
                .errors(errors)
                .build();

        return ResponseEntity.status(BAD_REQUEST).body(apiErrorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
            final @NonNull HandlerMethodValidationException ex,
            final @NonNull HttpHeaders headers,
            final @NonNull HttpStatusCode status,
            final @NonNull WebRequest request) {
        log.info(ex.getMessage(), ex);

        final List<ApiErrorDetails> errors = new ArrayList<>();
        for (final var validation : ex.getValueResults()) {
            final String parameterName = validation.getMethodParameter().getParameterName();
            validation.getResolvableErrors().forEach(error ->
                    errors.add(ApiErrorDetails.builder()
                            .pointer(parameterName)
                            .reason(error.getDefaultMessage())
                            .build())
            );
        }

        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .statusCode(BAD_REQUEST.value())
                .message("Validation failed.")
                .instance(request.getDescription(false))
                .errors(errors)
                .build();

        return ResponseEntity.status(BAD_REQUEST).body(apiErrorResponse);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleJakartaConstraintViolationException(
            final jakarta.validation.ConstraintViolationException ex, WebRequest request) {
        log.info(ex.getMessage(), ex);

        final List<ApiErrorDetails> errors = new ArrayList<>();
        for (final var violation : ex.getConstraintViolations()) {
            errors.add(ApiErrorDetails.builder()
                    .pointer(((PathImpl) violation.getPropertyPath()).getLeafNode().getName())
                    .reason(violation.getMessage())
                    .build());
        }

        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .statusCode(BAD_REQUEST.value())
                .message("Validation failed.")
                .instance(request.getDescription(false))
                .errors(errors)
                .build();

        return ResponseEntity.status(BAD_REQUEST).body(apiErrorResponse);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(
            final AccessDeniedException ex, WebRequest request) {
        log.info(ex.getMessage(), ex);

        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .message("Access Denied")
                .instance(request.getDescription(false))
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiErrorResponse);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiErrorResponse> handleAllExceptions(
            final Throwable ex, WebRequest request) {
        log.warn("{}", ex.getMessage(), ex);

        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(API_DEFAULT_ERROR_MESSAGE)
                .instance(request.getDescription(false))
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiErrorResponse);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalStateException(
            final IllegalStateException ex, WebRequest request) {
        log.info(ex.getMessage(), ex);

        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .statusCode(BAD_REQUEST.value())
                .message(ex.getMessage())
                .instance(request.getDescription(false))
                .build();

        return ResponseEntity.status(BAD_REQUEST).body(apiErrorResponse);
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiErrorResponse> accessDeniedException(
            final AuthorizationDeniedException ex, WebRequest request) {
        log.info(ex.getMessage(), ex);

        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .statusCode(UNAUTHORIZED.value())
                .message("Authorization Denied")
                .instance(request.getDescription(false))
                .build();

        return ResponseEntity.status(UNAUTHORIZED).body(apiErrorResponse);
    }
    
    
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleUsernameNotFoundException(
            final UsernameNotFoundException ex, WebRequest request) {
        log.info(ex.getMessage(), ex);

        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .statusCode(BAD_REQUEST.value())
                .message(ex.getMessage())
                .instance(request.getDescription(false))
                .build();

        return ResponseEntity.status(BAD_REQUEST).body(apiErrorResponse);
    }
}
