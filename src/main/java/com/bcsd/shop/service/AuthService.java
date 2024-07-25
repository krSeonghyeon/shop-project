package com.bcsd.shop.service;

import com.bcsd.shop.controller.dto.request.LoginRequest;
import com.bcsd.shop.controller.dto.response.UserInfoResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    UserInfoResponse login(LoginRequest loginRequest, HttpServletRequest httpServletRequest);
    void logout(HttpServletRequest request);
}
