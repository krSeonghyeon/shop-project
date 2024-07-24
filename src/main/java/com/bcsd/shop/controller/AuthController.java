package com.bcsd.shop.controller;

import com.bcsd.shop.controller.dto.request.LoginRequest;
import com.bcsd.shop.controller.dto.response.UserInfoResponse;
import com.bcsd.shop.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auths")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserInfoResponse> login(
            @RequestBody @Valid LoginRequest loginRequest,
            HttpServletRequest httpServletRequest
    ) {
        UserInfoResponse response = authService.login(loginRequest, httpServletRequest);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseEntity.noContent().build();
    }
}
