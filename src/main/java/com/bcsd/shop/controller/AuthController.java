package com.bcsd.shop.controller;

import com.bcsd.shop.controller.dto.request.LoginRequest;
import com.bcsd.shop.controller.dto.response.UserInfoResponse;
import com.bcsd.shop.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인증", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auths")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @PostMapping("/login")
    public ResponseEntity<UserInfoResponse> login(
            @RequestBody @Valid LoginRequest loginRequest,
            HttpServletRequest httpServletRequest
    ) {
        UserInfoResponse response = authService.login(loginRequest, httpServletRequest);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "로그아웃")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseEntity.noContent().build();
    }
}
