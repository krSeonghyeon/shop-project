package com.bcsd.shop.service;

import com.bcsd.shop.controller.dto.request.LoginRequest;
import com.bcsd.shop.controller.dto.response.UserInfoResponse;
import com.bcsd.shop.domain.User;
import com.bcsd.shop.exception.CustomException;
import com.bcsd.shop.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bcsd.shop.exception.errorcode.AuthErrorCode.AUTHENTICATION_ERROR;
import static com.bcsd.shop.exception.errorcode.AuthErrorCode.AUTHENTICATION_FAILED;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManager;
    private static final int SESSION_TIMEOUT_SECONDS = 1800;

    @Transactional
    public UserInfoResponse login(LoginRequest loginRequest, HttpServletRequest httpServletRequest) {
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.email(), loginRequest.password());

            Authentication authentication = authenticationManager.getObject().authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();

            createSession(httpServletRequest, user);

            return UserInfoResponse.from(user);
        } catch (UsernameNotFoundException | BadCredentialsException e) {
            throw new CustomException(AUTHENTICATION_FAILED);
        } catch (AuthenticationException e) {
            throw new CustomException(AUTHENTICATION_ERROR);
        }
    }

    private static void createSession(HttpServletRequest httpServletRequest, User user) {
        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute("userId", user.getId());
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        session.setMaxInactiveInterval(SESSION_TIMEOUT_SECONDS);
    }

    @Transactional
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            SecurityContextHolder.clearContext();
        }
    }
}
