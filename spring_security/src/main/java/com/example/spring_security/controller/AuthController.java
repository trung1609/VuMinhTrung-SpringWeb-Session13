package com.example.spring_security.controller;

import com.example.spring_security.dto.request.LoginRequest;
import com.example.spring_security.dto.request.UserRegister;
import com.example.spring_security.dto.response.ApiResponse;
import com.example.spring_security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> registerUser(@RequestBody UserRegister userRegister) {
        return new ResponseEntity<>(new ApiResponse<>(
                true,
                "User registered successfully",
                userService.registerUser(userRegister),
                HttpStatus.CREATED
        ), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> loginUser(@RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(new ApiResponse<>(
                true,
                "Login successful",
                userService.login(loginRequest),
                HttpStatus.OK
        ), HttpStatus.OK);
    }
}
