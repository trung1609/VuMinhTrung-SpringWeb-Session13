package com.example.spring_security.controller;

import com.example.spring_security.dto.request.LoginRequest;
import com.example.spring_security.dto.request.UserRegister;
import com.example.spring_security.dto.response.ApiResponse;
import com.example.spring_security.exception.ResourceConflictException;
import com.example.spring_security.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @GetMapping
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("Hello, World!", HttpStatus.OK);
    }

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> registerUser(@Valid @RequestBody UserRegister userRegister) throws ResourceConflictException {
        return new ResponseEntity<>(new ApiResponse<>(
                true,
                "User registered successfully",
                authService.registerUser(userRegister),
                HttpStatus.CREATED
        ), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> loginUser(@RequestBody LoginRequest loginRequest) throws Exception {
        return new ResponseEntity<>(new ApiResponse<>(
                true,
                "Login successful",
                authService.login(loginRequest),
                HttpStatus.OK
        ), HttpStatus.OK);
    }
}
