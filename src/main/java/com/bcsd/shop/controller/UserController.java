package com.bcsd.shop.controller;

import com.bcsd.shop.controller.dto.request.UserJoinRequest;
import com.bcsd.shop.controller.dto.response.UserInfoResponse;
import com.bcsd.shop.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<UserInfoResponse> join(@RequestBody @Valid UserJoinRequest request) {
        UserInfoResponse response = userService.join(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
