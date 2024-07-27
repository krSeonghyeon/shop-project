package com.bcsd.shop.controller;

import com.bcsd.shop.controller.dto.request.PasswordModifyRequest;
import com.bcsd.shop.controller.dto.request.SellerJoinRequest;
import com.bcsd.shop.controller.dto.request.UserInfoModifyRequest;
import com.bcsd.shop.controller.dto.request.UserJoinRequest;
import com.bcsd.shop.controller.dto.response.SellerInfoResponse;
import com.bcsd.shop.controller.dto.response.UserInfoResponse;
import com.bcsd.shop.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserInfoResponse> getUserInfo(
            @SessionAttribute(name = "userId", required = false) Long userId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("{}", authentication);

        return ResponseEntity.ok(userService.getUserInfo(userId));
    }

    @GetMapping("/seller")
    public ResponseEntity<SellerInfoResponse> getSellerInfo(
            @SessionAttribute(name = "userId") Long userId
    ) {
        return ResponseEntity.ok(userService.getSellerInfo(userId));
    }

    @PostMapping
    public ResponseEntity<UserInfoResponse> joinUser(@RequestBody @Valid UserJoinRequest request) {
        UserInfoResponse response = userService.join(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/seller")
    public ResponseEntity<SellerInfoResponse> joinSeller(@RequestBody @Valid SellerJoinRequest request) {
        SellerInfoResponse response = userService.joinSeller(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/password")
    public ResponseEntity<Void> modifyPassword(
            @SessionAttribute(name = "userId") Long userId,
            @RequestBody @Valid PasswordModifyRequest request
    ) {
        userService.modifyPassword(userId, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/info")
    public ResponseEntity<UserInfoResponse> modifyUserInfo(
            @SessionAttribute(name = "userId") Long userId,
            @RequestBody @Valid UserInfoModifyRequest request
    ) {
        UserInfoResponse response = userService.modifyUserInfo(userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(
            @SessionAttribute(name = "userId") Long userId
    ) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
