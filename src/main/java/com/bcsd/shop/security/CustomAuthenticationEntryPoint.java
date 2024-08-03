package com.bcsd.shop.security;

import com.bcsd.shop.controller.dto.response.CustomExceptionResponse;
import com.bcsd.shop.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.bcsd.shop.exception.errorcode.AuthErrorCode.UNAUTHORIZED_ACCESS;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        CustomExceptionResponse exceptionResponse = CustomExceptionResponse.from(UNAUTHORIZED_ACCESS);
        response.setStatus(UNAUTHORIZED_ACCESS.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(exceptionResponse));
    }
}
