package com.example.spring_security.exception;

import com.example.spring_security.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<?>> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(401).body(new ApiResponse<>(
                false,
                "Invalid username or password",
                null,
                HttpStatus.BAD_REQUEST
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage()
                ));
        return ResponseEntity.status(400).body(new ApiResponse<>(
                false,
                "Validation failed",
                errors,
                HttpStatus.BAD_REQUEST
        ));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse<?>> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(404).body(new ApiResponse<>(
                false,
                ex.getMessage(),
                null,
                HttpStatus.NOT_FOUND
        ));
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceConflictException(ResourceConflictException ex) {
        return ResponseEntity.status(409).body(new ApiResponse<>(
                false,
                ex.getMessage(),
                null,
                HttpStatus.CONFLICT
        ));
    }
}
