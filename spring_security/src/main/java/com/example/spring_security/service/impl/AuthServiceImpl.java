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
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public User registerUser(UserRegister userRegister) throws ResourceConflictException {
        if (userRepository.existsByEmail(userRegister.getEmail())) {
            throw new ResourceConflictException("Email is already in use: " + userRegister.getEmail());
        }

        if (userRepository.existsByPhone(userRegister.getPhone())) {
            throw new ResourceConflictException("Phone is already in use: " + userRegister.getPhone());
        }

        User user = new User();
        user.setEmail(userRegister.getEmail());
        user.setPassword(passwordEncoder.encode(userRegister.getPassword()));
        user.setRoles(mapRoleStringToRole(userRegister.getRoles()));
        user.setFullName(userRegister.getFullName());
        user.setPhone(userRegister.getPhone());

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
            return userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new NoSuchElementException("User not found with email: " + loginRequest.getEmail()));
        } catch (AuthenticationException ex) {
            throw new Exception("Invalid email or password");
        }

    }

    private Set<Role> mapRoleStringToRole(Set<RoleName> roles) {
        Set<Role> roleSet = new HashSet<>();
        if (roles != null && !roles.isEmpty()) {
            roles.forEach(role -> {
                switch (role) {
                    case ROLE_ADMIN:
                        roleSet.add(roleRepository.findByRoleName(role).orElseThrow(() -> new NoSuchElementException("Role not found: " + role)));
                        break;
                    case ROLE_USER:
                        roleSet.add(roleRepository.findByRoleName(role).orElseThrow(() -> new NoSuchElementException("Role not found: " + role)));
                        break;
                    default:
                        throw new NoSuchElementException("Role not found: " + role);
                }
            });
        } else {
            roleSet.add(roleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(() -> new NoSuchElementException("Role not found: ROLE_USER")));
        }
        return roleSet;
    }
}
