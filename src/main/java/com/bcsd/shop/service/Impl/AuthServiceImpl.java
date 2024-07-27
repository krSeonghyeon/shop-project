package com.bcsd.shop.service.Impl;

import com.bcsd.shop.controller.dto.request.LoginRequest;
import com.bcsd.shop.controller.dto.response.UserInfoResponse;
import com.bcsd.shop.domain.User;
import com.bcsd.shop.security.CustomUserDetails;
import com.bcsd.shop.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManagerBuilder authenticationManager;

    @Override
    @Transactional
    public UserInfoResponse login(LoginRequest loginRequest, HttpServletRequest httpServletRequest) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginRequest.email(), loginRequest.password());

        Authentication authentication = authenticationManager.getObject().authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = ((CustomUserDetails)authentication.getPrincipal()).getUser();

        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute("userId", user.getId());
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        session.setMaxInactiveInterval(1800);

        return UserInfoResponse.from(user);
    }

    @Override
    @Transactional
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            SecurityContextHolder.clearContext();
        }
    }
}
