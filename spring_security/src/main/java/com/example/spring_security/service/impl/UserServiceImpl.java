package com.example.spring_security.service.impl;

import com.example.spring_security.dto.request.LoginRequest;
import com.example.spring_security.dto.request.UserRegister;
import com.example.spring_security.entity.Role;
import com.example.spring_security.entity.User;
import com.example.spring_security.repository.RoleRepository;
import com.example.spring_security.repository.UserRepository;
import com.example.spring_security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public User registerUser(UserRegister userRegister) {

        if (userRepository.existsByUsername(userRegister.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(userRegister.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = User.builder()
                .username(userRegister.getUsername())
                .password(passwordEncoder.encode(userRegister.getPassword()))
                .fullName(userRegister.getFullName())
                .address(userRegister.getAddress())
                .email(userRegister.getEmail())
                .phone(userRegister.getPhone())
                .roles(mapRoleStringToRole(userRegister.getRoles()))
                .enabled(true)
                .build();
        return userRepository.save(user);
    }

    @Override
    public String login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()
        );

        Authentication authentication = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "Login successful! ROLE: " + authentication.getAuthorities();
    }

    private List<Role> mapRoleStringToRole(List<String> roles) {
        List<Role> roleList = new ArrayList<>();
        if (roles != null && !roles.isEmpty()) {
            roles.forEach(role -> {
                switch (role) {
                    case "ROLE_ADMIN":
                        roleList.add(roleRepository.findByRoleName(role).orElseThrow(() -> new NoSuchElementException("Role not found: " + role)));
                        break;
                    case "ROLE_USER":
                        roleList.add(roleRepository.findByRoleName(role).orElseThrow(() -> new NoSuchElementException("Role not found: " + role)));
                        break;
                    case "ROLE_MODERATOR":
                        roleList.add(roleRepository.findByRoleName(role).orElseThrow(() -> new NoSuchElementException("Role not found: " + role)));
                        break;
                    default:
                        roleList.add(roleRepository.findByRoleName("ROLE_USER").orElseThrow(() -> new NoSuchElementException("Role not found: " + role)));
                }
            });
        } else {
            roleList.add(roleRepository.findByRoleName("ROLE_USER").orElseThrow(() -> new NoSuchElementException("Role not found: ROLE_USER")));
        }
        return roleList;
    }
}
