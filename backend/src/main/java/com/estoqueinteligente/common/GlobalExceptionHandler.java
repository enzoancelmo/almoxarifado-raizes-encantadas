package com.estoqueinteligente.common;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import com.estoqueinteligente.auth.AuthenticationFailedException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> validation(MethodArgumentNotValidException exception, ServletWebRequest request) {
        Map<String, String> fields = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> fields.putIfAbsent(error.getField(), error.getDefaultMessage()));
        return build(HttpStatus.BAD_REQUEST, "Dados inválidos", request, fields);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ApiError> unreadable(HttpMessageNotReadableException exception, ServletWebRequest request) {
        return build(HttpStatus.BAD_REQUEST, "Dados inválidos ou incompletos", request, Map.of());
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ApiError> notFound(ResourceNotFoundException exception, ServletWebRequest request) {
        return build(HttpStatus.NOT_FOUND, exception.getMessage(), request, Map.of());
    }
    @ExceptionHandler(AuthenticationFailedException.class)
    ResponseEntity<ApiError> authentication(AuthenticationFailedException exception, ServletWebRequest request) {
        return build(HttpStatus.UNAUTHORIZED, exception.getMessage(), request, Map.of());
    }
    @ExceptionHandler(BusinessException.class)
    ResponseEntity<ApiError> business(BusinessException exception, ServletWebRequest request) {
        return build(HttpStatus.CONFLICT, exception.getMessage(), request, Map.of());
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ApiError> conflict(DataIntegrityViolationException exception, ServletWebRequest request) {
        return build(HttpStatus.CONFLICT, "Operação não permitida: o registro está em uso ou possui dados duplicados", request, Map.of());
    }
    private ResponseEntity<ApiError> build(HttpStatus status, String message, ServletWebRequest request, Map<String, String> fields) {
        ApiError error = new ApiError(Instant.now(), status.value(), status.getReasonPhrase(), message, request.getRequest().getRequestURI(), fields);
        return ResponseEntity.status(status).body(error);
    }
}
