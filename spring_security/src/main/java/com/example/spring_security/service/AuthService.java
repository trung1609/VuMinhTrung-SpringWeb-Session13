package com.example.spring_security.service;

import com.example.spring_security.dto.request.LoginRequest;
import com.example.spring_security.dto.request.UserRegister;
import com.example.spring_security.entity.User;
import com.example.spring_security.exception.ResourceConflictException;

public interface AuthService {
    User registerUser(UserRegister userRegister) throws ResourceConflictException;
    User login(LoginRequest loginRequest) throws Exception;
}
