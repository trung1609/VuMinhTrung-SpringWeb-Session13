package com.example.spring_security.security.config;

import com.example.spring_security.dto.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final String EXPIRED_TOKEN = "EXPIRED_TOKEN";
    private final String INVALID_TOKEN = "INVALID_TOKEN";
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String exception = (String) request.getAttribute("exception");
        ErrorResponse errorResponse = buildErrorResponse(exception, request);
        new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
    }

    private ErrorResponse buildErrorResponse(String exception, HttpServletRequest request){
        if (EXPIRED_TOKEN.equals(exception)){
            return new ErrorResponse(
                    "FAIL",
                    "Token has expired - Please login again",
                    EXPIRED_TOKEN,
                    request.getServletPath()
            );
        }
        return new ErrorResponse(
                "FAIL",
                "Invalid token - Please provide a valid token",
                INVALID_TOKEN,
                request.getServletPath()
        );
    }
}
