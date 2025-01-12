package com.example.springdata.restaurantbackend.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        String message;
        if (authException.getCause() instanceof AccountNotEnabledException) {
            message = authException.getCause().getMessage();
        } else if (authException.getCause() instanceof AccountNotVerifiedException) {
            message = authException.getCause().getMessage();
        } else {
            message = "Unauthorized access";
        }

        response.sendError(HttpStatus.UNAUTHORIZED.value(), message);
    }
}
