package com.bcsd.shop.service.Impl;

import com.bcsd.shop.controller.dto.request.LoginRequest;
import com.bcsd.shop.controller.dto.response.UserInfoResponse;
import com.bcsd.shop.domain.User;
import com.bcsd.shop.repository.UserRepository;
import com.bcsd.shop.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserInfoResponse login(LoginRequest loginRequest, HttpServletRequest httpServletRequest) {

        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new BadCredentialsException("잘못된 계정정보입니다."));

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new BadCredentialsException("잘못된 계정정보입니다.");
        }

        httpServletRequest.getSession().invalidate();
        HttpSession session = httpServletRequest.getSession(true);

        session.setAttribute("userId", user.getId());
        session.setMaxInactiveInterval(1800);

        return UserInfoResponse.from(user);
    }

    @Override
    @Transactional
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
