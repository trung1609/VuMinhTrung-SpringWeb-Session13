package com.example.spring_security.service.impl;

import com.example.spring_security.dto.request.LoginRequest;
import com.example.spring_security.dto.request.UserRegister;
import com.example.spring_security.entity.User;
import com.example.spring_security.exception.ResourceConflictException;
import com.example.spring_security.repository.UserRepository;
import com.example.spring_security.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public User registerUser(UserRegister userRegister) throws ResourceConflictException {
        if(userRepository.findByUsername(userRegister.getUsername()).isPresent()) {
            throw new ResourceConflictException("Username is already taken");
        }


        User user = new User();
        user.setUsername(userRegister.getUsername());
        user.setPassword(passwordEncoder.encode(userRegister.getPassword()));
        user.setFullName(userRegister.getFullName());

        if (userRegister.getRole() == null || userRegister.getRole().isEmpty() ) {
            user.setRole("USER");
        }else {
            user.setRole(userRegister.getRole());
        }
        user.setEnabled(true);
        return userRepository.save(user);
    }

    @Override
    public User login(LoginRequest loginRequest) throws Exception {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), loginRequest.getPassword()
            );

            Authentication authentication = authenticationManager.authenticate(token);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            return userRepository.findByUsername(loginRequest.getEmail())
                    .orElseThrow(() -> new NoSuchElementException("User not found with email: " + loginRequest.getEmail()));
        } catch (AuthenticationException ex) {
            throw new Exception("Invalid email or password");
        }

    }

}
