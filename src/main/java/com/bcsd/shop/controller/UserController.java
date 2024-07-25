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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    public ResponseEntity<UserInfoResponse> getUserInfo(
            @SessionAttribute(name = "userId") Long userId
    ) {
        return ResponseEntity.ok(userService.getUserInfo(userId));
    }

    @GetMapping("/info/seller")
    public ResponseEntity<SellerInfoResponse> getSellerInfo(
            @SessionAttribute(name = "userId") Long userId
    ) {
        return ResponseEntity.ok(userService.getSellerInfo(userId));
    }

    @PostMapping("/join")
    public ResponseEntity<UserInfoResponse> join(@RequestBody @Valid UserJoinRequest request) {
        UserInfoResponse response = userService.join(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/join/seller")
    public ResponseEntity<SellerInfoResponse> joinSeller(@RequestBody @Valid SellerJoinRequest request) {
        SellerInfoResponse response = userService.joinSeller(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/modify/password")
    public ResponseEntity<Void> modifyPassword(
            @SessionAttribute(name = "userId") Long userId,
            @RequestBody @Valid PasswordModifyRequest request
    ) {
        userService.modifyPassword(userId, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("modify/info")
    public ResponseEntity<UserInfoResponse> modifyUserInfo(
            @SessionAttribute(name = "userId") Long userId,
            @RequestBody @Valid UserInfoModifyRequest request
    ) {
        UserInfoResponse response = userService.modifyUserInfo(userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(
            @SessionAttribute(name = "userId") Long userId
    ) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
