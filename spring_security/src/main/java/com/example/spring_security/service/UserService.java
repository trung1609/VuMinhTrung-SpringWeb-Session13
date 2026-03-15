package com.example.spring_security.service;

import com.example.spring_security.dto.request.LoginRequest;
import com.example.spring_security.dto.request.UserRegister;
import com.example.spring_security.entity.User;

public interface UserService {
    User registerUser(UserRegister userRegister);
    String login(LoginRequest loginRequest);
}
