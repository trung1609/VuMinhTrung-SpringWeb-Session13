package com.example.spring_security.service.impl;

import com.example.spring_security.dto.LoginResponse;
import com.example.spring_security.dto.request.LoginRequest;
import com.example.spring_security.dto.request.UserRegister;
import com.example.spring_security.entity.User;
import com.example.spring_security.exception.ResourceConflictException;
import com.example.spring_security.repository.UserRepository;
import com.example.spring_security.security.jwt.JwtProvider;
import com.example.spring_security.security.principle.UserDetailServiceCustom;
import com.example.spring_security.security.principle.UserPrincipal;
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
    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public String registerUser(UserRegister userRegister) throws ResourceConflictException {
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
        userRepository.save(user);
        return "User registered successfully";
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) throws Exception {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        }catch (AuthenticationException e) {
            throw new ResourceConflictException("Invalid username or password");
        }

        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        String token = jwtProvider.generateToken(userDetails.getUsername());

        return LoginResponse.builder()
                .username(userDetails.getUsername())
                .role(userDetails.getAuthorities().stream().findFirst().orElseThrow(() -> new NoSuchElementException("No role found")).getAuthority())
                .token(token)
                .build();
    }

}
